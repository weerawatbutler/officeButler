package com.example.weerawatposeeya.officebutler;

public class MyConstanst {
    private String host = "ftp.swiftcodingthai.com";
    private String user = "ann@swiftcodingthai.com";
    private  String password = "Abc12345";
    //private  Int port = 21;
    private String urlAddUserString = "http://swiftcodingthai.com/ann/addUserMasterAndroid.php";
    private String prefixString = "http://swiftcodingthai.com/ann/Tikk";
    private String urlGetAllUser  = "http://swiftcodingthai.com/ann/getAllUser.php";

    public String getUrlGetAllUser() {
        return urlGetAllUser;
    }

    public String getPrefixString() {
        return prefixString;
    }

    public String getUrlAddUserString() {
        return urlAddUserString;
    }

    public String getHost() {

        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

//    public Int getPort() {
//        return port;
//    }
}
