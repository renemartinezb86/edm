package com.ericsson.dev.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Created by ezmarre on 02/19/2019.
 */
@Service
public class BSCSDataService {

    private final OracleConnection oracleConnection;

    public BSCSDataService(OracleConnection oracleConnection) {
        this.oracleConnection = oracleConnection;
    }

    public HashMap<String, List<HashMap>> getCustomerPlans(OracleConnection connection) {
        String query = "select o.customer_id CUENTA, o.co_id CONTRATO, o.co_code CO_CODE, c.lbc_date FECHA_ULTIMO_FACTURAMENTO, c.CSACTIVATED DECHA_ACTIVACION, sh.status ESTADO_SERVICIO\n" +
            ",      sh.valid_from_date FECHA_SERVICIO, o.tmcode PLANO, xn.sncode SERVICIO, ph.spcode PAQUETE, sp.DES NOMBRE_PLANO, sp.EXTERNAL_SERVICE_PACKAGE_NAME\n" +
            ",      xn.CFS_SPECIFICATION_ID, xn.RFS_SPECIFICATION_ID, xn.POP_ID, tmb.accessfee PRECIO_MESUAL\n" +
            "from   ( select count(1) qtd, c.customer_id customer_id from contract_all c where c.plcode = 56 and c.tmcode = 3 and c.ch_status in ('a','s') group by c.customer_id having count(1) >= 2 ) multi\n" +
            ",      contract_all        o\n" +
            ",      customer_all        c\n" +
            ",      profile_service     p\n" +
            ",      pr_serv_status_hist sh\n" +
            ",      pr_serv_spcode_hist ph\n" +
            ",      mpusptab            sp\n" +
            ",      mpulkpxn            xn\n" +
            ",      curr_mpulktmb       tmb\n" +
            "where  --o.customer_id = 5081\n" +
            "--and    \n" +
            "o.customer_id = multi.customer_id\n" +
            "and    o.tmcode = 3 \n" +
            "and    o.ch_status in ('a','s')\n" +
            "and    o.plcode = 56 \n" +
            "and    o.customer_id = c.customer_id\n" +
            "and    o.co_id = p.co_id\n" +
            "and    p.co_id = sh.co_id\n" +
            "and    p.profile_id = sh.profile_id\n" +
            "and    p.sncode = sh.sncode\n" +
            "and    p.STATUS_HISTNO = sh.histno\n" +
            "and    p.co_id = ph.co_id\n" +
            "and    p.profile_id = ph.profile_id\n" +
            "and    p.sncode = ph.sncode\n" +
            "and    p.SPCODE_HISTNO = ph.histno\n" +
            "and    ph.spcode = sp.spcode\n" +
            "and    sp.spcode = xn.spcode\n" +
            "and    p.sncode = xn.sncode\n" +
            "--and    xn.POP_ID like '%RECURR%'\n" +
            "and    o.tmcode = tmb.tmcode\n" +
            "and    xn.sncode = tmb.sncode\n" +
            "and    xn.spcode = tmb.spcode\n" +
            "and    tmb.accessfee > 0\n" +
            "and    o.customer_id in (15854406, 15846368)";
        try {
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            HashMap result = resultSetToHashMap(rs, "CUENTA");
            ps.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void executeBatch(OracleConnection connection, List<String> discountSQLs) throws SQLException {
        Connection conn = connection.getConnection();
        Statement statement = conn.createStatement();
        for (String sql : discountSQLs) {
            //statement.addBatch(sql);
            statement.execute(sql);
        }
        //statement.executeBatch();
        statement.close();
        conn.close();
    }

    public HashMap<String, List<HashMap>> resultSetToHashMap(ResultSet rs, String fieldId) throws SQLException {

        ResultSetMetaData md = rs.getMetaData();
        HashMap<String, List<HashMap>> result = new HashMap<>();
        int columns = md.getColumnCount();
        List<HashMap> plans;
        HashMap row = new HashMap();
        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                if (rs.getObject(i) != null) {
                    if (md.getColumnName(i).equalsIgnoreCase("FECHA_ULTIMO_FACTURAMENTO")) {
                        row.put(md.getColumnName(i), rs.getDate(i));
                    } else {
                        row.put(md.getColumnName(i), rs.getObject(i).toString());
                    }
                } else {
                    row.put(md.getColumnName(i), "");
                }
            }
            if (!result.containsKey("" + row.get(fieldId))) {
                plans = new ArrayList<>();
                result.put("" + row.get(fieldId), plans);
            }
            result.get("" + row.get(fieldId)).add(row);
            row = new HashMap();
        }
        return result;
    }
}
