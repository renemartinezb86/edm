package com.ericsson.dev.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ezmarre on 02/19/2019.
 */

@Service
public class OracleConnection {
    private Connection bconnection = null;
    /*private static String url = "jdbc:oracle:thin:@10.49.4.52:1530/EEOCMD03.tdenopcl.internal";
    private static String buser = "ECM_V3";
    private static String bpass = "ECM_V3";*/

    private String url = "jdbc:oracle:thin:@10.49.7.144:1591/EEOCMPP1.tdenopcl.internal";
    private String buser = "ECM";
    private String bpass = "ECM";

    public OracleConnection() {
        super();
    }

    public Connection getConnection() {
        String ebs_query = "";
        if (bconnection == null) {
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                bconnection = DriverManager.getConnection(url, buser, bpass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bconnection;
    }

    public void closeConnection() throws SQLException {
        bconnection.close();
    }

    public OracleConnection(String url, String buser, String bpass) {
        this.url = url;
        this.buser = buser;
        this.bpass = bpass;
    }

    //public static void main(String[] args) {
//        try {
//            String query =
//                "SELECT SUM(amount/100) as AMOUNT ,MSISDN FROM CVL_REFILL where MSISDN = ? AND REFILL_STATUS='FINALIZADA' AND REQUEST_DATE <= '14-NOV-17 11.59.59.000000000 PM' AND TYPE ='RECARGA' AND PAYMENT_METHOD not in ('POINTS','PRESTA LUKA') group by MSISDN";
//            List<Subscriber> subscribers = readSubscriberList("Subscribers.txt");
//            PreparedStatement ps = null;
//            for (int i = 0; i < subscribers.size(); i++) {
//                Subscriber su = subscribers.get(i);
//                ps = getConnection().prepareStatement(query);
//                ps.setString(1, su.getMsisdn());
//                ResultSet rs = ps.executeQuery();
//                String amount = "0";
//                while (rs.next()) {
//                    amount = rs.getString("AMOUNT");
//                }
//                //Aqui le acabo de poner esto para ignorar los que tengan saldo 0
//                if (!amount.equals("0")) {
//                    String line = su.getMsisdn() + "|" + amount + "|" + su.getRut();
//                    System.out.println("Subscriptores: " + i);
//                    log("C:\\Users\\Proyecto\\Documents\\JDeveloper\\CreateUpdateSusbcriber\\InsertSubscribersCVL\\result.txt",
//                        line, true);
//
//                }
//                ps.close();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    //}

    private static void log(String pathFile, String line, boolean append) {
//        PrintWriter out = null;
//        try {
//            File file = new File(pathFile);
//            file.getParentFile().mkdirs();
//            out = new PrintWriter(new BufferedWriter(new FileWriter(pathFile, append)));
//            out.println(line);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            if (out != null) {
//                out.flush();
//                out.close();
//            }
//        }
    }
//
//    @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
//    public static List<Subscriber> readSubscriberList(String file) {
//
//        String csvFile = file;
//        String line = "";
//        String cvsSplitBy = "\\|";
//        List<Subscriber> subscriberList = new LinkedList<Subscriber>();
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//            while ((line = br.readLine()) != null) {
//                String[] data = line.split(cvsSplitBy);
//                Subscriber subscriber = new Subscriber();
//                int i = 0;
//                subscriber.setRut(data[i++]);
//                subscriber.setMsisdn(data[i++]);
//                subscriberList.add(subscriber);
//            }
//            System.out.println(subscriberList.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return subscriberList;
//    }
}
