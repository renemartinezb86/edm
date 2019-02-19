package com.ericsson.dev.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ezluipi on 12/26/2018.
 */
@Service
public class BSCSDataService implements InitializingBean {

    private final OracleConnection oracleConnection;

    public BSCSDataService(OracleConnection oracleConnection) {
        this.oracleConnection = oracleConnection;
    }

    public List<String> getActivePoList() throws SQLException {

        String query = "select external_service_package_name from mpusptab where external_service_package_name IS NOT NULL";
        PreparedStatement ps = oracleConnection.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        LinkedList<String> poList = new LinkedList<>();
        while (rs.next()) {
            poList.add(rs.getString("external_service_package_name"));
        }
        ps.close();

        return poList;
    }

    public List<String> getActiveTemplates(OracleConnection connection) throws SQLException {

        String query = "select SHDES from rfs_specification";
        PreparedStatement ps = connection.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        LinkedList<String> templatesList = new LinkedList<>();
        while (rs.next()) {
            templatesList.add("BSCS_"+rs.getString("SHDES"));
        }
        ps.close();

        return templatesList;
    }


    public static void main(String[] args) throws SQLException {
        BSCSDataService bscsDataService = new BSCSDataService(new OracleConnection("jdbc:oracle:thin:@10.49.4.46:1530/EBSCSD01.tdenopcl.internal", "SYSADM", "SYSADM"));
        //        List<String> poList = ecmDataService.getPoList();
        //        for (String po : poList
        //            ) {
        //            System.out.println(ecmDataService.getActiveCharacteristicsByPOWithNameAsId(po));
        //        }
        //        for (String po : poList
        //            ) {
        //            System.out.println(po);
        //            ecmDataService.getRelationsByPO(po);
        //        }
        //     ecmDataService.getRelationsByPO("PO_POS_O_SS_MM_CCAP");
        //ecmDataService.getActiveCharacteristicsByPOWithNameAsId("PO_POS_A_BOLSA_15DIAS_500MB_BAL");
        //bscsDataService.getActivePoList();
        //bscsDataService.getActiveTemplates();

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //getPoList();
    }
}
