package com.treeke.grpc;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/4
 * @Version 1.0
 **/
public class CARead extends JPanel {
    private String CA_Name;
    private String CA_ItemData[][] = new String[9][2];
    private String[] columnNames = {"证书字段标记","内容" };
    public CARead(String CertName) {
        CA_Name=CertName;
        /* 三个Panel用来显示证书内容*/
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel panelNormal = new JPanel();
        tabbedPane.addTab("普通信息", panelNormal);
        JPanel panelAll=new JPanel();
        panelAll.setLayout(new BorderLayout());
        tabbedPane.addTab("所有信息",panelAll);
        JPanel panelBase64=new JPanel();
        panelBase64.setLayout(new BorderLayout());
        tabbedPane.addTab("Base64编码信息",panelBase64);
        /* 读取证书常规信息 */
        Read_Normal(panelNormal);
        /* 读取证书文件字符串表示内容 */
        Read_Bin(panelAll);
        /* 读取证原始Base64编码形式的证书文件 */
        Read_Raw(panelBase64);
        tabbedPane.setSelectedIndex(0);
        setLayout(new GridLayout(1, 1));
        add(tabbedPane);
    }
            /*以下是定义的Read_Normal()，Read_Bin(),Read_Raw()以及main()
            这里省略...   */

    private int Read_Normal(JPanel panel){
        String Field;
        try{
            CertificateFactory certificate_factory= CertificateFactory.getInstance("X.509");
            FileInputStream file_inputstream=new FileInputStream(CA_Name);
            X509Certificate
                    x509certificate=(X509Certificate)certificate_factory.generateCertificate
                    (file_inputstream);
            Field=x509certificate.getType();
            CA_ItemData[0][0]="类型";
            CA_ItemData[0][1]=Field;
            Field=Integer.toString(x509certificate.getVersion());
            CA_ItemData[1][0]="版本";
            CA_ItemData[1][1]=Field;
            Field=x509certificate.getSubjectDN().getName();
            CA_ItemData[2][0]="标题";
            CA_ItemData[2][1]=Field;
            // 以下类似，这里省略
            Field=x509certificate.getNotBefore().toString();
            CA_ItemData[3][0]="有效日期";
            CA_ItemData[3][1]=Field;
            Field=x509certificate. getNotAfter().toString();
            CA_ItemData[4][0]="截止日期";
            CA_ItemData[4][1]=Field;
            Field=x509certificate.getSerialNumber().toString(16);
            CA_ItemData[5][0]="序列号";
            CA_ItemData[5][1]=Field;
            Field=x509certificate.getIssuerDN().getName();
            CA_ItemData[6][0]="发行者名";
            CA_ItemData[6][1]=Field;
            Field=x509certificate.getSigAlgName();
            CA_ItemData[7][0]="签名算法";
            CA_ItemData[7][1]=Field;
            Field=x509certificate.getPublicKey().getAlgorithm();
            CA_ItemData[8][0]="公钥算法";
            CA_ItemData[8][1]=Field;
            file_inputstream.close();
            final JTable table = new JTable(CA_ItemData, columnNames);
            TableColumn tc=null;
            tc = table.getColumnModel().getColumn(1);
            tc.setPreferredWidth(600);
            panel.add(table);
        }catch(Exception exception){
            exception.printStackTrace();
            return -1;
        }
        return 0;
    }

    private int Read_Bin(JPanel panel){
        try{
            FileInputStream file_inputstream=new FileInputStream(CA_Name);
            DataInputStream data_inputstream=new DataInputStream(file_inputstream);
            CertificateFactory certificatefactory=CertificateFactory.getInstance("X.509");
            byte[] bytes=new byte[data_inputstream.available()];
            data_inputstream.readFully(bytes);
            ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
            JEditorPane Cert_EditorPane;
            Cert_EditorPane=new JEditorPane();
            while(bais.available()>0){
                X509Certificate
                        Cert=(X509Certificate)certificatefactory.generateCertificate(bais);
                Cert_EditorPane.setText(Cert_EditorPane.getText()+Cert.toString());
            }
            Cert_EditorPane.disable();
            JScrollPane edit_scroll=new JScrollPane(Cert_EditorPane);
            panel.add(edit_scroll);
            file_inputstream.close();
            data_inputstream.close();
        }catch( Exception exception){
            exception.printStackTrace();
            return -1;
        }
        return 0;
    }

    private int Read_Raw(JPanel panel){
        try{
            JEditorPane Cert_EditorPane=new JEditorPane();
            String CertText=null;
            File inputFile = new File(CA_Name);
            FileReader in = new FileReader(inputFile);
            char[] buf=new char[2000];
            int len=in.read(buf,0,2000);
            for(int i=1;i<len;i++)
            {
                CertText=CertText+buf[i];
            }
            in.close();
            Cert_EditorPane.setText(CertText);
            Cert_EditorPane.disable();
            JScrollPane edit_scroll=new JScrollPane(Cert_EditorPane);
            panel.add(edit_scroll);
        }catch( Exception exception){
            exception.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("证书阅读器");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        String path = CARead.class.getClassLoader().getResource("").getPath();
        frame.getContentPane().add(new CARead(path + "ca.cer"),BorderLayout.CENTER);
        frame.setSize(700, 425);
        frame.setVisible(true);
    }
   /* public static void main(String[] args) throws UnknownHostException {
        Resolver resolver = new SimpleResolver("8.8.8.8");
        Name name = ReverseMap.fromAddress(InetAddress.getByName("14.215.177.38"));
        Lookup lookup = new Lookup(name, 12);
        lookup.setResolver(resolver);
        Record[] records = lookup.run();
        int result = lookup.getResult();
        PTRRecord ptr = (PTRRecord)records[0];
        String s = ptr.getTarget().toString();

        InetAddress[] addresses = null;
        try {
            String name1 = "www.baidu.com";
            addresses = InetAddress.getAllByName(name1);
            for (int i = 0; i < addresses.length; i++) {
                System.out.println(name + "[" + i + "]: "
                        + addresses[i].getHostAddress());
            }
        } catch (UnknownHostException uhe) {
            System.err.println("Unable to find: " + args[0]);
        }

        String hostName = null;
        try {
            hostName = Address.getHostName(InetAddress.getByName("220.181.38.148"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(hostName);
    }*/

}
