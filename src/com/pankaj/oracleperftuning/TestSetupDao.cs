using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Text;
using Oracle.ManagedDataAccess.Client;
using Oracle.ManagedDataAccess.Types;

namespace BindVariablesDemonstration
{
    public class TestSetupDao
    {

        public static int SetupTest(String description)
        {            
            FlushSharedSqlArea();
            int runNumber = GetRunNumber();
            CapturePerformanceStats(runNumber, description, 1);
            return runNumber;
        }


        private static void FlushSharedSqlArea()
        {
            String connectionString = ConfigurationManager.ConnectionStrings["DBA"].ConnectionString; ;
            using (OracleConnection con = new OracleConnection(connectionString))
            {
                con.Open();
                using (OracleCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = "ALTER SYSTEM FLUSH SHARED_POOL";
                    cmd.CommandType = CommandType.Text;

                    cmd.ExecuteNonQuery();                    
                }
            }           
        }


        private static void CapturePerformanceStats(int runNumber, String description, int dataSequence)
        {
            String connectionString = ConfigurationManager.ConnectionStrings["STUDENT"].ConnectionString; ;
            using (OracleConnection con = new OracleConnection(connectionString))
            {
                con.Open();
       
                CaptureCpuStatistics(con, runNumber, description, dataSequence);                
                CaptureParseStatistics(con, runNumber, description, dataSequence);
                CaptureLatchStatistics(con, runNumber, description, dataSequence);
            }

        }


        #region SQL for capturing and reporting on database performance metrics

        private const String CAPTURE_CPU_STATS =
            @"INSERT INTO performance_capture_stats
                  (run_number, description, data_sequence, stat_name, stat_value)
              SELECT :run_number, :description, :data_sequence, stat_name, value 
                  FROM v$sys_time_model
                  WHERE stat_name = 'DB CPU'";


        private const String CAPTURE_PARSE_STATS_SQL =
            @"INSERT INTO performance_capture_stats
                  (run_number, description, data_sequence, stat_name, stat_value)
              SELECT :run_number, :description, :data_sequence, name, value 
                  FROM v$sysstat WHERE statistic# IN (771, 772, 774)";


        private const String CAPTURE_LATCH_DATA_SQL =
            @"INSERT INTO performance_capture_stats
                  (run_number, description, data_sequence, stat_name, stat_value)
              SELECT
                 :run_number, :description, :data_sequence, 
                 CASE pivot
                     WHEN 1 THEN  'shared pool gets'
                     WHEN 2 THEN  'shared pool misses'
                     WHEN 3 THEN  'shared pool sleeps'
                     WHEN 4 THEN  'shared pool spin gets'
                     ELSE NULL
                 END stat_name,
                 CASE pivot
                     WHEN 1 THEN gets
                     WHEN 2 THEN misses
                     WHEN 3 THEN sleeps
                     WHEN 4 THEN spin_gets
                     ELSE NULL
                 END stat_value
                 FROM  v$latch,
                 (SELECT rownum pivot from dual CONNECT BY LEVEL <=4)
             WHERE name = 'shared pool' ";



        public const String PERF_STATS_SQL =
            @"SELECT 
                a.stat_name, 
                (b.stat_value - a.stat_value) / ps.divisor as value,
                ps.units
            FROM performance_capture_stats a
            INNER JOIN performance_capture_stats b
                ON a.stat_name = b.stat_name 
                AND a.run_number = b.run_number
            INNER JOIN perf_stats ps
                ON a.stat_name = ps.stat_name
            WHERE a.run_number = :run_number
                AND a.data_sequence = 1
                AND b.data_sequence = 2
            ORDER BY ps.display_order";

        #endregion



        private static int GetRunNumber()
        {
            int runNumber = 0;
            String connectionString = ConfigurationManager.ConnectionStrings["STUDENT"].ConnectionString; ;
            using (OracleConnection con = new OracleConnection(connectionString))
            {
                con.Open();
                using (OracleCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = "SELECT seq_performance_capture.nextval FROM dual";

                    runNumber = Convert.ToInt32(cmd.ExecuteScalar());
                }
            }
            return runNumber;
        }


        private static void CaptureCpuStatistics(OracleConnection con, int runNumber, String desc, int dataSequence)
        {
            using (OracleCommand cmd = con.CreateCommand())
            {
                cmd.CommandText = CAPTURE_CPU_STATS;
                cmd.Parameters.Add(new OracleParameter(":run_number", runNumber));
                cmd.Parameters.Add(new OracleParameter(":description", desc));
                cmd.Parameters.Add(new OracleParameter(":data_sequence", dataSequence));

                cmd.ExecuteNonQuery();
            }
        }





        private static void CaptureParseStatistics(OracleConnection con, int runNumber, String desc, int dataSequence)
        {
            using (OracleCommand cmd = con.CreateCommand())
            {
                cmd.CommandText = CAPTURE_PARSE_STATS_SQL;
                cmd.Parameters.Add(new OracleParameter(":run_number", runNumber));
                cmd.Parameters.Add(new OracleParameter(":description", desc));
                cmd.Parameters.Add(new OracleParameter(":data_sequence", dataSequence));

                cmd.ExecuteNonQuery();
            }

        }





        private static void CaptureLatchStatistics(OracleConnection con, int runNumber, String desc, int dataSequence)
        {
            using (OracleCommand cmd = con.CreateCommand() )
            {
                cmd.CommandText = CAPTURE_LATCH_DATA_SQL;
                cmd.Parameters.Add(new OracleParameter(":run_number", runNumber));
                cmd.Parameters.Add(new OracleParameter(":description", desc));
                cmd.Parameters.Add(new OracleParameter(":data_sequence", dataSequence));

                cmd.ExecuteNonQuery();
            }
        }








        public static void FinishTest(int runNumber, String description)
        {
            CapturePerformanceStats(runNumber, description, 2);
        }





        public static List<PerformanceStat> GetPerformanceStats(int runNumber)
        {
            List<PerformanceStat> stats = new List<PerformanceStat>();

            String connectionString = ConfigurationManager.ConnectionStrings["STUDENT"].ConnectionString; ;
            using (OracleConnection con = new OracleConnection(connectionString))
            {
                con.Open();
                using (OracleCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = PERF_STATS_SQL;                    

                    cmd.Parameters.Add(new OracleParameter(":run_number", runNumber));

                    using (OracleDataReader reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            PerformanceStat stat = new PerformanceStat();
                            stat.Name = reader.GetString(0);
                            stat.Value = reader.GetDecimal(1);
                            stat.Units = reader.GetString(2);
                            stats.Add(stat);
                        }
                    }
                }
            }
            return stats;
        }



        #region Unused Code

        private static int TakeStatspackSnaphot()
        {
            int snapshotId = -1;

            String connectionString = ConfigurationManager.ConnectionStrings["STUDENT"].ConnectionString; ;
            using (OracleConnection con = new OracleConnection(connectionString))
            {
                con.Open();
                using (OracleCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = "statspack.snap";
                    cmd.CommandType = CommandType.StoredProcedure;

                    OracleParameter snapshotIdParm = new OracleParameter(":snapshot_id", OracleDbType.Decimal, ParameterDirection.ReturnValue);
                    cmd.Parameters.Add(snapshotIdParm);

                    cmd.ExecuteNonQuery();

                    OracleDecimal id = (OracleDecimal)snapshotIdParm.Value;
                    snapshotId = id.ToInt32();
                }
            }
            return snapshotId;
        }

        #endregion

    }
}
