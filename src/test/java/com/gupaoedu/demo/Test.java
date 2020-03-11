package com.gupaoedu.demo;

/**
 * @Author wangcunlei
 * @Date 2020-3-6 10:38
 * @Description
 */
public class Test {

    public static void main(String[] args) {
        String appSumSql = "SELECT M.report_name AS name,sum,num AS usernum,sum/num AS avg FROM "
            + "( "
            + "  SELECT report_name,SUM(length_time) sum FROM mos_report r LEFT JOIN tb_user u ON r.created_user_id = u.user_id "
            + "  LEFT JOIN (SELECT * FROM mos_app WHERE is_deleted = 0) a ON r.platform = a.platform AND r.pkg = a.pkg_name AND r.version_code = a.version_code AND r.version_name = a.version_name "
            + "  WHERE u.is_deleted = 0 AND a.app_id IS NOT NULL AND r.start_time >= :start AND r.start_time <= :end "
            + "  GROUP BY report_name "
            + ") M "
            + "LEFT JOIN "
            + "( "
            + " SELECT report_name,SUM(usernum) num FROM "
            + " ( "
            + "  SELECT report_name,platform,pkg,version_code,version_name,COUNT(created_user_id) AS usernum FROM "
            + "  ( "
            + "   SELECT r.created_user_id,appname AS report_name,platform,app_pkg_name AS pkg,app_version_code AS version_code,app_version_name AS version_name FROM mos_device d ,mos_report_device r WHERE d.securityid= r.securityid "
            + "   UNION "
            + "   SELECT r.created_user_id,report_name,platform,pkg,version_code,version_name FROM mos_report r LEFT JOIN tb_user u ON r.created_user_id = u.user_id WHERE u.is_deleted = 0 AND r.start_time >= :start AND r.start_time <= :end "
            + "   GROUP BY created_user_id,report_name,platform,pkg,version_code,version_name "
            + "  ) A "
            + "  GROUP BY report_name,platform,pkg,version_code,version_name "
            + " ) B "
            + " GROUP BY report_name "
            + ") N "
            + "ON M.report_name = N.report_name ";
        System.out.println(appSumSql);
            /*+ "order by " + order + " "
            + "limit " + pageStart + "," + pageSize;*/


        String userSumSql =
            "select u.user_id,u.user_name,u.login_name,d.dept_name,sum(sum_time) sum "
                + "  from mos_report_sum_user s,tb_user u,tb_department d "
                + "  where s.created_user_id = u.user_id and u.department_id = d.dept_id AND u.is_deleted = 0"
                + "    and full_time >= :start and full_time <= :end"
                + "  group by s.created_user_id";
               /* + "  order by " + order
                + "  limit " + pageStart + "," + pageSize;*/

        System.out.println(userSumSql);
    }
}
