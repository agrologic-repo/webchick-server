<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Java Butcher</title>
</head><body><%@ page import="java.sql.*" %>
<%
Connection dbCon;
String url = "jdbc:mysql://localhost?user=root&password=agrologic";
try {
Class.forName("com.mysql.jdbc.Driver");
dbCon = DriverManager.getConnection(url);
Statement st = dbCon.createStatement();st.execute("Create database if not exists agrodb");
try {
st.execute("use agrodb");
st.execute("Create table if not exists users(UserID int(10) unsigned NOT NULL DEFAULT NULL auto_increment,Name varchar(45) NOT NULL DEFAULT NULL ,Password varchar(45) NOT NULL DEFAULT NULL ,FirstName varchar(45) NOT NULL DEFAULT NULL ,LastName varchar(45) NOT NULL DEFAULT NULL ,Role int(11) NOT NULL DEFAULT NULL ,State smallint(5) unsigned NOT NULL DEFAULT NULL ,Phone varchar(45) NOT NULL DEFAULT NULL ,Email varchar(45) NOT NULL DEFAULT NULL ,Company varchar(45) NOT NULL DEFAULT NULL ,Primary Key (UserID))");
} catch(Exception e) {};
st.execute("insert into users values ('1','valery','dmFsZXJ5','Valery','Manakhimov','1','0','+97298626089','valery@agrologic.com','Agrologic')");
st.execute("insert into users values ('2','demo','ZGVtbw==','DemoUser','DemoUser','1','0','+972000000000','info@agrologic.com','Agrologic')");
st.execute("insert into users values ('5','arie','YXJpZQ==','Arie','Haham','2','2','+972544425900','4425900@gmail.com','Arie')");
st.execute("insert into users values ('6','paul','cG91bA==','Paul','Paul','2','1','+972528903781','','Arie')");
st.execute("insert into users values ('7','yariv','eWFyaXY=','Yariv','Shfaim','2','1','','','Arie')");
st.execute("insert into users values ('8','ofer','ofer','Ofer','Ofer','2','1','+972523750840','','')");
st.execute("insert into users values ('9','yoram','yoram','Yoram','Livne','2','1','0544428254','','')");
st.execute("insert into users values ('14','plasson','plasson','plasson','plasson','2','0','','','Plasson')");
st.execute("insert into users values ('16','joel','am9lbA==','Joel','Obodov','2','0','0544571523','joel@agrologic.com','Agrologic')");
st.execute("insert into users values ('18','avi','avi','Avi ','Avivim','2','1','0507275754','','')");
st.execute("insert into users values ('19','yuri','yuri','Yuri','Seroshtanov','2','1','','','')");
st.execute("insert into users values ('20','aron','arongal','Aron','Gal','2','1','0507966272','','')");
st.execute("insert into users values ('21','guy','guy','Guy','Klumeck','2','1','0528813153','clumeck@017.net.il','')");
st.execute("insert into users values ('23','joe','joe','Johanan','Hershtik','1','0','0544571530','johanan@agrologic.com','Agrologic')");
st.execute("insert into users values ('26','moshe','green','Moshe','Green','2','1','0578196649','yotam1988@hotmail.com','')");
st.execute("insert into users values ('27','rami','rami','Rami','Kadash','2','1','0577304357','','')");
st.execute("insert into users values ('28','dubani','dubani','Shai','Dubani','2','1','','','')");
st.execute("insert into users values ('29','paulmc','paulmc','Paul','McConn','2','1','','mcconnpaul@eircom.net','PE Services')");
st.execute("insert into users values ('30','ziv','eml2YXJiZWw=','Doron','Turgman','2','2','','','')");
st.execute("insert into users values ('32','yohav','eW9oYXY=','Kibbutz Malkia','Yohav','2','2','0525347864','','')");
st.execute("insert into users values ('33','ctb','Y3Ri','CTBUser','CTBUser','2','2','','','Agrologic')");
st.execute("insert into users values ('34','Carmia','a2FybWlh','Carmia','Carmia','2','2','0508993090','','')");
st.execute("insert into users values ('35','betalfa','YmV0YWxmYQ==','kibbutz','Bet Alfa','2','2','0544921242','','')");
st.execute("insert into users values ('36','ziv','eml2','Mishmar','Hasharon','2','2','0523317683','','')");
st.execute("insert into users values ('37','asv','YXN2','Sde','Trumot','2','2','0523654499','','')");
st.execute("insert into users values ('38','pardo','cGFyZG8=','Pardo','Bet Halevi','2','2','0541234567','','')");
st.execute("insert into users values ('39','kobygeva','a29ieWdldmE=','Koby','Geva','2','0','0544242369','kobygeva@bezeqint.net','')");
st.execute("insert into users values ('40','betremon','YmV0cmVtb24=','Asher','Asher','2','2','0544509662','asafchen6@gmail.com','Bet Remon')");
st.execute("insert into users values ('41','asaf','NjQ0ODMxMw==','Asaf','Beizeyhen','2','2','0542241115','','Beizeyhen')");
st.execute("insert into users values ('42','efratgeza','ZWZyYXRnZXph','Zev','Zev','2','2','0546921121','','')");
st.execute("insert into users values ('44','proyectos','cHJveWVjdG9z','Lujan','Muruaga - Devak','2','2','','lmuruaga@devak-co.com','Proyectos')");
st.execute("insert into users values ('46','Alonim','MTIxMzE0','Ethan','Avidov','2','2','0528709410','lulal@alonim.org.il','Kibbutz Alonim')");
st.execute("insert into users values ('47','chinagx5farm','MTIzNA==','China GX','5 farms','2','2','','gxservice@cnguangxing.com','GX')");
st.execute("insert into users values ('48','chinagx12farm','MTIzNA==','China GX','12 farms','2','2','','gxservice@cnguangxing.com','GX')");
st.execute("insert into users values ('49','kelly','MjEzMTE=','Kelly','Seamuse','2','2','','','PE Services')");
st.execute("insert into users values ('50','IFS','MTIzNA==','Martyn ','Fisher','2','2','','','IFS')");
st.execute("insert into users values ('51','ehxr','MjAwMA==','Otiy','Kesar','2','2','0544412191','','')");
st.execute("insert into users values ('52','ron','cm9u','Ron','Yardenay','2','2','0508825181','','')");
st.execute("insert into users values ('53','kadarim','MTIzNA==','Mark','Kadarim','2','2','0528391475','','')");
st.execute("insert into users values ('54','masd','MTIzNA==','Masada','Masada','2','2','','','')");
st.execute("insert into users values ('55','brn','OTk0NQ==','Lior','Baranis','2','2','0547799946','','')");
st.execute("insert into users values ('57','Manit 1','MTIzNA==','Tamir','Hachamisha','2','2','0544921618','','Maanit')");
st.execute("insert into users values ('59','mesilot','MTIzNA==','egal','lul','2','2','+972546754464','','')");
st.execute("insert into users values ('61','oren','MTcxMg==','Oren','Salamon','2','2','+97254-4578499','','')");
st.execute("insert into users values ('63','zivarbel','eml2YXJiZWw=','ziv','Mismar Hasharom','2','2','+972523317683','','Mishmar Hashamron')");
st.execute("insert into users values ('65','ross','MzE1MTE=','Ross','McHenry','2','2','','','PE Services')");
st.execute("insert into users values ('66','adnan','MTIzNA==','Adnan','Kalkilia','2','2','0527350349','','')");
st.execute("insert into users values ('68','nll','NDIwMw==','Moran','Ein Hashofet','2','2','0507594203','','Ein Hashofet')");
st.execute("insert into users values ('69','ein','NjE5OA==','Rafee','EinHashofet','2','2','0507536198','','Ein Hashofet')");
st.execute("insert into users values ('70','maors3','JDI0MTA3MA==','Maor','Shraiber','2','2','0528746302','maorshriber@walla.co.il','')");
st.execute("insert into users values ('71','lulgonen','bHVsZ29uZW4=','Eran','Gonen','2','2','0502001224','','')");
st.execute("insert into users values ('72','dudut','ODg1ODU4','Dudu','Tavor','2','2','0524799599','','')");
st.execute("insert into users values ('74','unisa','MTIzNA==','University ','South Australia ','2','2','','','IFS')");
st.execute("insert into users values ('77','alonim3-4','MzM0NA==','Ethan','Avidov','2','2','0528709410','','Kibbutz Alonim')");
out.println("<h2>All data successfully batched</h2>");}catch(Exception e) {
e.printStackTrace();
}
%>

<%response.sendRedirect("./all-users.html");%>
</body></html>