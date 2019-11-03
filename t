[33mcommit 1e7e9de7028d09d4703d52b42db23e984217aa3b[m[33m ([m[1;36mHEAD -> [m[1;32mwebchick-6.7.58[m[33m)[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Mon Nov 4 00:45:18 2019 +0200

    removed readonly user group

[33mcommit a265156cda54f3a03188d6d7390b883af472783e[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Mon Nov 4 00:40:35 2019 +0200

    #4 #7 #25 fixed daily history by skipping checksum error during receiveng history data

[33mcommit b9d227821162eb7f0b88888fa0ff8822c0be0ff0[m[33m ([m[1;31morigin/webchick-6.7.58[m[33m)[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Mon Apr 15 15:52:36 2019 +0300

    added all changes that was done since version 6.7.9

[33mcommit 7142d890a4427566c571ed3555018ba6d4c2ad07[m[33m ([m[1;31morigin/webchick-6.7.57[m[33m, [m[1;31morigin/master[m[33m, [m[1;32mwebchick-6.7.57[m[33m, [m[1;32mmaster[m[33m)[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 27 14:40:30 2015 +0300

    Revision 6.7.9
    
    Added move row up and down on screens , tables, and data .
    Added polish language to web

[33mcommit 32bb98ae8c4a63fbc02d15c6bd10f144728eca22[m
Merge: ff5c259 76bfac8
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 5 12:45:40 2015 +0300

    Merge remote-tracking branch 'origin/master'

[33mcommit ff5c2590e51999dfa8a048c41df083553caa3df2[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 5 12:44:56 2015 +0300

    Added new Data IDs to the database
     Changed Data ID titles .

[33mcommit 76bfac8ae3de0af38a2734ed054421c8d7d70aa7[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Sun Mar 1 18:44:04 2015 +0200

    Update README.md

[33mcommit d2646b0f64e725af984435e476adecc5bf8987df[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jan 27 14:01:18 2015 +0200

    Revision 6.7.7
    
    Fixed issue #1491        WCL - if program id not same the data not aligned to the line
    Fixed issue #1488        WCL - is system state is too long it does not fit to size of text box

[33mcommit 53120c02305785e68f19e30cc601b88bf741122d[m
Author: val100 <valery@agrologic.com>
Date:   Wed Jan 21 16:08:11 2015 +0200

    Revision 6.7.7
    
    Fixed issue #1478 from pit.agrologic.com - when flock added and flock name not in english it is became not readable text
    Fixed issue #1475 from pit.agrologic.com - add translation for screens tables and data add encoded translation and it should be in UTF-8 format
    Fixed issue #1472 from pit.agrologic.com -  when controller is added there is encoding function that encode controller title

[33mcommit b32fcf890ee59413b9983866ba13a1607dc0ee7b[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jan 13 13:22:35 2015 +0200

    Revision 6.7.6
    
    Fixed issue #1431 from pit.agrologic.com -  when Alarm type 2 (ID : 3170) and Alarm Type 3 (ID : 3708) appear ALARM ICON does not displayed on controller name panel

[33mcommit f6ee5e1a41468a2d3bf152da4904cc6912badcd5[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jan 13 13:15:55 2015 +0200

    Revision 6.7.6
    
    Fixed issue #1431 from pit.agrologic.com -  when Alarm type 2 (ID : 3170) and Alarm Type 3 (ID : 3708) appear ALARM ICON does not displayed on controller name panel

[33mcommit 4c61c3eaaab6c2cc217e86f8e96d8473a08d5f9b[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 25 12:49:42 2014 +0200

    Fixed issue #1454 from pit.agrologic.com - cellink time does not updated on MainScreen and ControllerScreen is open this issue cause disconnect from cellink after few minutes

[33mcommit dd086343c5c2e7267585f68891d14e68b8b28535[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 25 12:43:56 2014 +0200

    Fixed issue #1451 from pit.agrologic.com - alarm list disappears very quickly , maximum time 10 sec . it is not enough to read all list

[33mcommit 75b78e393c939ebe7e6f9605bec4b8732fc8816e[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 22 11:26:59 2014 +0200

    Revision 6.7.5
    
    Fixed issue #1332 from pit.agrologic.com - when label of data is too long , the layout of tables are changed according to this length and it is spoil the view

[33mcommit 7ffe92a7cade19c13d057e76852f56926eac1f13[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 22 10:23:03 2014 +0200

    Fixed bugs&issues :
            #1326    WC - alarm list can show actual alarms in controller in red color
            #1427    WC - replace current alarm popup with new one using jquery
            #1434    WC - when Alarm type 2 (ID : 3170) and Alarm Type 3 (ID : 3708) appear ALARM ICON does not displayed on controller name panel
            #1422    WC - on add-data page in language combobox missing languages German, Turkish,Polish

[33mcommit 93fa6067df8c1dbfabae197582fcd624864d463d[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 22 09:56:48 2014 +0200

    Fixed issue #1431 from pit.agrologic.com - when Alarm type 2 (ID : 3170) and Alarm Type 3 (ID : 3708) appear ALARM ICON does not displayed on controller name panel

[33mcommit b2d6b73989b4de82be204030b84b1dc2bc3f441f[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 18 15:27:49 2014 +0200

    Fixed issue #1376 from pit.agrologic.com -  add program page have to add option to copy also system states, alarms, relays, special data

[33mcommit 27c7f1c1ec60526ea469dd7980f675796c2dabb0[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 18 15:18:18 2014 +0200

    Fixed issue #1404 from pit.agrologic.com - when I click on "Action Set Button" screen it change language to default language "English"

[33mcommit 57dd643a984e1429ecf2553b1d78928e1ce40a6d[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 18 15:16:15 2014 +0200

    Fixed issue #1404 from pit.agrologic.com - when I click on "Action Set Button" screen it change language to default language "English"

[33mcommit 958ba700488d67447b85ea0fb676bef6fb024764[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 18 15:12:41 2014 +0200

    Revision 6.7.4
    
    Fixed issue #1320 from pit.agrologic.com - add CO2 graph to flock history by graw day

[33mcommit a7c029accf33b37130f9db6c36bf88f434c38973[m
Author: val100 <valery@agrologic.com>
Date:   Wed Dec 17 10:21:45 2014 +0200

    Revision 6.7.4
    
    Fixed issue #1320 from pit.agrologic.com - add CO2 graph to flock history by graw day

[33mcommit adc6360d265d2157be6537c36c2a10dd14178afa[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 15 16:00:21 2014 +0200

    Fixed issue #1413 from pit.agrologic.com - add manual to webchick web version

[33mcommit 071568d2b4ef510b08542463c86c561b38442c3b[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 15 11:20:19 2014 +0200

    Moved my-farms.jsp to SpringMVC
     - replaced scriplet with jstl tag
     - #52 replaced custom i18n with spring i18n

[33mcommit 28baf358b6f2c29895621ba40ee2f89bd21745d3[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 14 15:44:29 2014 +0200

    Moved my-farms.jsp to SpringMVC

[33mcommit d0a70e817ed211129a4de27d78cc1d2f417c1243[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 14 15:40:49 2014 +0200

    Moved my-farms servlet to SpringMVC
     - Created a controller for myfarms

[33mcommit 0c83977e10852eba9571b947440a2675db2a5c31[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 14 14:50:45 2014 +0200

    Fixed issue #1401 from pit.agrologic.com - change to read only system state number on system state assign page
    Fixed issue #1398 from pit.agrologic.com - change to read only alarm number field on alarm assign page
    Fixed issue #1395 from pit.agrologic.com - change to read only bit numbers on relays assign page

[33mcommit e5c1d126c7ae3b93025b31d0ea1ff5a707b280d8[m
Author: val100 <valery@agrologic.com>
Date:   Thu Dec 11 12:32:22 2014 +0200

    Moved clear-controller-data servlet to SpringMVC
     - Created a controller for clear-controller-data

[33mcommit 949323bd67b2cfee4a10916c2c2e416df1d1b561[m
Author: val100 <valery@agrologic.com>
Date:   Mon Dec 1 16:10:17 2014 +0200

    Fixed issue #1381 from pit.agrologic.com - fter delete screen from table some data remain in table "tabledata"

[33mcommit e4ee712f0696551280b9c1de3bf7da4b9819a561[m
Author: val100 <valery@agrologic.com>
Date:   Wed Nov 26 11:56:31 2014 +0200

    Fixed issue #1371 from pit.agrologic.com - changing data value without reload whole page in main screen page and controller data page

[33mcommit 0b9c64ec75af7bb9e5aac5058b86ddc75a4c2184[m
Author: val100 <valery@agrologic.com>
Date:   Mon Nov 24 09:11:30 2014 +0200

    Fixed issue #1339 from pit.agrologic.com - on edit controller data page house name in hebrew became xxx instead of לול

[33mcommit e7abda2b900c1c0383f48f6ddda6b0bdcde857a8[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 11 10:29:39 2014 +0200

    Fixed issue #1309 from pit.agrologic.com - button "clear" on history per hour graph does not work correctly

[33mcommit dff3ee7c4f29a140908f0d0c182a8d76e049043a[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 11 10:11:37 2014 +0200

     fixed some bugs

[33mcommit 5cdf1fa2ea34d548b6c5b094b0832ba8d40a8264[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 22 09:08:28 2014 +0300

    Fixed issue #1298 from pit.agrologic.com - paging.jsp does note change direction according to language that choosed

[33mcommit 6ebcb18d6f4e1d4665ab06f7dc4117fc2aa24661[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 22 09:00:23 2014 +0300

    Fixed issue #1294 from pit.agrologic.com - clear unused action also clean tables that used to fix layouts of tables on screen

[33mcommit 620276f399c1f6fff145a4a0fff1f4d7ab9777e0[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 15 12:15:08 2014 +0300

    #56 fixed export per hour history data

[33mcommit 1f96b72f6c18518a69296dc86e4dddf0fd427a5c[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 15 12:11:10 2014 +0300

    #54 fixed disconnecting cellink if state is RUNNING

[33mcommit bc2916752c901ee6b42fb0222d7956529bc759f9[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 15 08:41:16 2014 +0300

    #55 fixed

[33mcommit 38be0b4708f4239ea5c522f7604b58044f50722e[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 7 14:37:27 2014 +0300

    #54 fixed disconnecting cellink if state is RUNNING

[33mcommit 37665c9bfe8a765d71e85d10fee3dc92f5f2dd53[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 7 12:30:04 2014 +0300

    #53 fixed when we added into <excludes>  'rmtctrl-flock*' and 'dashboard*' pages

[33mcommit 08bf984f46ccf47c7da606a757d28126a564009c[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 7 11:52:24 2014 +0300

    #53 fixed when we added into <excludes>  'rmtctrl-flock*' and 'dashboard*' pages

[33mcommit 73fd088402cf66117c5bc273a6d567b6b6250613[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 17:25:52 2014 +0300

    #52 replaced custom i18n with spring i18n

[33mcommit e5e736ef7079058396b617442cab11066c6e5c25[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 16:49:26 2014 +0300

    Added default exception handler to page overview .

[33mcommit ea732fdcb0eb380057e9317affe96a28ca35a137[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 16:27:30 2014 +0300

    #51 fixed and tested pagination

[33mcommit 94237ea44ac721e2c01a3279a55de7388715a6f1[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 15:52:41 2014 +0300

    Replaced forward with  redirect for disconnecting cellinks .
    
    - wrong address while we where staying on overview page

[33mcommit 09b93c9bf959eb27c84f1b776ccb9aad42665f12[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 14:35:34 2014 +0300

    Changes style according to HTML5

[33mcommit 151efabaa9abb14c896e0c0c7790270982aa023a[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 11:43:10 2014 +0300

    Move static resources from jsp to decorator

[33mcommit dd9d8719a285e6135c65c763bac10549d090130f[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 10:50:59 2014 +0300

    duplicate <html> in page

[33mcommit 761e84c46dcee19728f56868033db24dce1bf1eb[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 10:38:18 2014 +0300

    Add decorator for overview page
    
    - To get rid of duplications in every pages

[33mcommit 785b467894a7349a94b68402e5e72d3f183d0d9e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 09:46:02 2014 +0300

    Added translations .

[33mcommit c08d759672c157c0f359d50823c2ec22cb160f53[m
Author: val100 <valery@agrologic.com>
Date:   Sun Oct 5 09:45:16 2014 +0300

    Changes in graphs pages .

[33mcommit 6d5927e4f60fd1565590039c68d4c58db1914fd7[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 30 14:02:38 2014 +0300

    Fixed issue #1112       from pit.agrologic.com - history data downloading each time all data from even if filter is only on per hour data .It should be separated actions .
    
    In addition fixed bugs from from pit.agrologic.com
    
    - #873 - flock history data export to excel and to table is shifted one column left in STD data
    - #990 - add progress icon on loading flock history page
    - #1055 - when filtering history by grow day after refresh page it not go to the history by day graph
    
    also fixed and optimized many other issues and bugs .

[33mcommit e9822538954006b62bf75c3e6585ab53bf381ba7[m
Author: val100 <valery@agrologic.com>
Date:   Mon Sep 29 15:30:45 2014 +0300

    Fixed issue #1260       from pit.agrologic.com - on flock history table "Grow Day" column became second when "Requested Temperature" is in list of history data

[33mcommit e4742bb876b29907584aa65072ee183f05be6dd2[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 17:42:31 2014 +0300

    Moved overview.jsp to Spring MVC
    
    - Moved JSP under WEB-INF
    - Created a controller for that JSP

[33mcommit ee51ed09e17d46f10bd6ef116b640ac483f090e6[m
Author: ctapobep <ctapobep@javatalks.ru>
Date:   Sun Sep 14 15:14:11 2014 +0400

    Added `@Autowired` to the Controller
    
    Also, now the contexts are loaded when the web app is loaded (added
    listener to web.xml).

[33mcommit f38726ccb7b41d67f2411a43daad805c7ff13676[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 13:15:35 2014 +0300

    #49 Moved rmctrl-main-screen.jsp to Spring MVC
    
    - Moved JSP under WEB-INF
    - Created a controller for that JSP
    - Removed old CellinkName logic since now we don't jsp-include it, but
     rather pass all required data from controller
    
    Additionally:
    - toplang.jsp was changed since it was using `request.requestURL` while
     that field was changed by jsp-include to `WEB-INF/blah.jsp`

[33mcommit 4fc7af31b957920f3741e9c96e4f17badc8fcb6c[m
Author: ctapobep <ctapobep@javatalks.ru>
Date:   Sun Sep 14 11:57:57 2014 +0400

    #49 Added Spring MVC and first controller
    
    - Added Spring MVC dependency into Maven.
    - Added SpringDispatcher (Front Controller) to web.xml so that it
     intercepts requests and chooses a controller that's responsible for
     the request handling.
    - Implemented a simple CellinkController that just outputs the cellink
     name by its id. Old CellinkNameServlet is to be removed.
    - Added a simple JSP into WEB-INF that outputs the information passed
     from Controller.

[33mcommit 50e1f07e7849c3e14cf35456b43df3e4b34ff3ac[m
Author: ctapobep <ctapobep@javatalks.ru>
Date:   Sun Sep 14 11:53:42 2014 +0400

    Added admin/admin user to cleandb.sql and also a cellink for it
    
    Additionally removed `use agrodb_for_tests` because when we apply
    script using `mysql dbname < cleandb.sql` we already specify the DB to
    work with.

[33mcommit a6794d12af87f7e47c943bda6ef385be35bdff8d[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 09:52:16 2014 +0300

    Added UI related files for webchick web and local

[33mcommit 0ca47aebafaf9288514d476f9b4db7e1fea6a20a[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 09:48:50 2014 +0300

    Removed webchick-local/agrodb, it shouldn't be added to git.

[33mcommit 7b937c6a9dda0b09ed21b0b4a89b9cb354f45f60[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 09:34:04 2014 +0300

    Fixed issues

[33mcommit 714605144a8a6036b0446ef074dc362a46b2a20a[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 09:33:07 2014 +0300

    Fixed UI issues

[33mcommit 45f92c9986ed9dabb28f8d4871aaa4f28b47731e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 14 09:11:53 2014 +0300

    Lot of changes

[33mcommit c7e68c1b130230e538900515d608930f54c59004[m
Author: val100 <valery@agrologic.com>
Date:   Thu Jul 24 13:44:46 2014 +0300

    Fixed issue #967 from pit.agrologic.com - add action set buttons screen

[33mcommit 1b794795cf778914a98f37909648fd09993d8408[m
Author: val100 <valery@agrologic.com>
Date:   Thu Jul 24 12:21:01 2014 +0300

    Fixed issue #1184 from pit.agrologic.com - 24 hour graph with humidity data not show correct values

[33mcommit 0c9d63a39326ed5c182e931086dfbb012c4bd7df[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jul 14 13:51:53 2014 +0300

    Fixed issue #89 from pit.agrologic.com - add index policies . When user wants to change data he press on enter . Next selected text field must be selected

[33mcommit 614b634871b545a478bca95a2ac6393efda9014c[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jul 14 13:43:53 2014 +0300

    Fixed issue #1145 from pit.agrologic.com  once program starting table with data to change must be empty

[33mcommit d736805e8142f8bfae3072d8d07c24af9849d2d9[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jul 14 13:40:53 2014 +0300

    Fixed issue #1145 from pit.agrologic.com  once program starting table with data to change must be empty

[33mcommit 053753d9abea334c5f9ad9c66917944e9e4de553[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 23 09:40:43 2014 +0300

    Fixed issue #493 in pit  add button that will automatically uncheck unusable data from screens , unused tables , unused screens .

[33mcommit 22f97635ba324cbf8ff0b4c13568a3abb25e4614[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jun 10 17:38:12 2014 +0300

    Fixed issue #1080       from pit when component orientation changed to right-to-left the value also changes orientation this cause sign '-' appear at right side

[33mcommit ac067f23a0d2e95e95683fa553db9c78656e7dc9[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jun 10 17:32:10 2014 +0300

    Fixed issue #1076       issue in pit  focus on text field does not lost issue affects on updating value that in focus

[33mcommit 457797c06745ef44ec3007e9e58a1212fe1e01cf[m
Author: val100 <valery@agrologic.com>
Date:   Thu Jun 5 13:06:53 2014 +0300

    Fixed issue #1058        WCL - set horizontal scroll bar of second screen to the left side in hebrew and to the right side in english

[33mcommit e9933de377081ad60d8d6aefe4fae80511ef3ee8[m
Author: val100 <valery@agrologic.com>
Date:   Thu Jun 5 12:57:50 2014 +0300

    Fixed issue #1058       in pit.agrologic.com set horizontal scroll bar of second screen to the left side in hebrew and to the right side in english

[33mcommit af7c041f4b66c66cde4b96bad2fc1eb5e9953d96[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:40:39 2014 +0300

    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit e3ff5466673d27d9e8fb55295e3f9653dec62072[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:40:13 2014 +0300

    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit dd21403ab0502e10b7b98f2c9396dc4812f66e61[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:40:02 2014 +0300

    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit 30c1c15a1e52798726117c3046fddb3922956020[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:39:49 2014 +0300

    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit af98d94239cbe2b83285c7ef5d8bb902a58a0031[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:39:33 2014 +0300

    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit 9394cbda5f33085c4c41af3fc29b6555226b1365[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 16:24:56 2014 +0300

    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit 79802f5a3e624a6b3b820951a19839720587c38b[m
Merge: ddb97d7 86d9a62
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 15:51:44 2014 +0300

    Merge remote-tracking branch 'origin/master'

[33mcommit ddb97d7ddf8a87824c2609e718a1beb7a042439a[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 15:51:08 2014 +0300

    Fixed issue #1019 from pit.agrologic.com added protocol to status bar

[33mcommit 86d9a62d943dd54a0f1e47d34e1b9be4e4936f5e[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 2 15:16:16 2014 +0300

    Fixed issue #1019 from pit.agrologic.com add communication protocol to status bar
    Fixed issue #1009 from pit.agrologic.com create translation for all system messages
    Fixed issue #984 from pit.agrologic.com add right-to-left language features
    Fixed issue #933 from pit.agrologic.com add export to excel service to flock graphs
    Fixed issue #891 from pit.agrologic.com add index policies . When user wants to change data he press on enter . Next selected text field must be selected
    Fixed bug #981 from  pit.agrologic.com change input characters to * for password data
    Fixed bug #940  from  pit.agrologic.com on manage flock chick quantity male and female should be have DEC_4 format
    Fixed bug #936  from  pit.agrologic.com format value to decimal point set comma instead of dot when Locale is "Turkish" or "Chineese"
    Fixed bug #926  from pit.agrologic.com add translation to alarm popup dialog for title text "Alarms"
    Fixed bug #168  from pit.agrologic.com On alarm 2 popup list the text shifted up, the first line can not be read

[33mcommit beac5272b14975026800fe85787244ed807e2600[m
Author: val100 <valery@agrologic.com>
Date:   Thu Apr 24 11:16:15 2014 +0300

    Fixed bug #940 from pit.agrologic.com . Chicks quantity format fixed to DEC_4

[33mcommit aa8767281ec95c10eb952e4322d37ceff9a004d5[m
Author: val100 <valery@agrologic.com>
Date:   Thu Apr 24 10:27:45 2014 +0300

    Fixed issue #933 from pit.agrologic.com . Added  export to excel service to flock graphs .

[33mcommit 1f2b7910a83a7568a56cf7651f2406a7fc33a377[m
Author: val100 <valery@agrologic.com>
Date:   Thu Apr 24 09:50:19 2014 +0300

    Fixed issue #891 from pit.agrologic.com project WebchickLocal . The focus move to next text field when Enter pressed .

[33mcommit b14d1cfac8d0a3dfffc4f6a32e4dc3e0da1f36e9[m
Author: val100 <valery@agrologic.com>
Date:   Wed Apr 23 16:49:15 2014 +0300

    - fixed  bug #168 in pit.agrologic.com project WebchickLocal

[33mcommit ae3175c1e00eeee2524e961c6786bef4a40200ee[m
Author: val100 <valery@agrologic.com>
Date:   Sun Jan 12 09:33:14 2014 +0200

    - Migrate database table 'datatable' . This action associated with the addition of fields and values (historyopt, historydnum).

[33mcommit a489f5e260092e30b85ca25f02c2706ee3d5b0c8[m
Author: val100 <valery@agrologic.com>
Date:   Sun Jan 12 09:31:30 2014 +0200

    Revision and updates
    - Revision version to 6.6.4
    - Renamed webchick-domain to webchick-common because this module contain common API for web , server and local modules .
    - Added webchick-service  module . This module contain all services .
    - Added service history :
     -- responsible for interacting with database tables that contain history data values of controllers .
     -- also contain exporting history data to excel service
     -- also contain creation history data to html table service
    - Migrate database table 'datatable' . This action associated with the addition of fields and values (historyopt, historydnum).
    - Removed unused classes and methods .

[33mcommit 1e57644ae5e9d4ad694365e79779dd3f5239d9ea[m
Author: val100 <valery@agrologic.com>
Date:   Tue Dec 17 16:55:04 2013 +0200

    Revision and updates
    - Added controller house type .
    - Fixed updating flock data
    - Fixed inserting screens
    - Added fields to eggs table
    - Added exception
    - Fixed flock graphs issues
    - Added using Executor to update screens data

[33mcommit 06af02e226f997b57ee53ae664751276bcda7d2e[m
Author: val100 <valery@agrologic.com>
Date:   Tue Dec 17 16:52:17 2013 +0200

    Revision and updates
    - Added controller house type .
    - Fixed updating flock data
    - Fixed inserting screens
    - Added fields to eggs table
    - Added exception
    - Fixed flock graphs issues
    - Added using Executor to update screens data

[33mcommit 8038524ab74b35b9e33f73e465b41a8343de1200[m
Author: val100 <valery@agrologic.com>
Date:   Tue Dec 17 10:28:42 2013 +0200

    Issue #48 fixed

[33mcommit 7068002a1d4cadc5bbf5627fe69ad991b6888894[m
Author: val100 <valery@agrologic.com>
Date:   Wed Dec 4 11:37:34 2013 +0200

    Issue #47 fixed

[33mcommit 5b73928339805cb4bec97f592335d59b23ba14f6[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 1 13:26:23 2013 +0200

    Issue #46 fixed

[33mcommit eb0a202d6a2e04a181bee11e2883c142a528e586[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 1 13:25:52 2013 +0200

    Issue #45 fixed

[33mcommit 1c3d322cc15cf57854ebba584675bf698c013624[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 1 13:24:07 2013 +0200

    Issue #44 fixed

[33mcommit 491dcb08701557d00dbf799b9e912d01da803b6e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Dec 1 13:20:14 2013 +0200

    Issue #43 fixed

[33mcommit 388cf9c616b796bde5171ae1e844bddcdaca3713[m
Author: val100 <valery@agrologic.com>
Date:   Wed Nov 27 12:34:59 2013 +0200

    Issue #42 fixed

[33mcommit 263cd8bf38df537dc343d2d630d8fdbb5f861898[m
Author: val100 <valery@agrologic.com>
Date:   Mon Nov 25 12:54:43 2013 +0200

    Issue #40 fixed

[33mcommit f8030c1e929f22d65197fda0a87c5d8fd5f813c0[m
Author: val100 <valery@agrologic.com>
Date:   Mon Nov 25 12:51:22 2013 +0200

    Issue #41 fixed

[33mcommit 1587e5adda5fdb71d410b12f2248a5d60e711a8d[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 21 13:24:38 2013 +0200

    Fixed issue insert translation into action set translation table

[33mcommit 827d2edae953b739d61ac07bade0e102547e2b5b[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 21 12:38:21 2013 +0200

     fixed issue graph shows shifted series data on time line

[33mcommit e9d6a415eb168dd1f86b6c91d716db8024e1ca03[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 14 16:34:21 2013 +0200

    Issue #39 - Copy s screen table not work correctly fixed

[33mcommit dc1bb7b55fe48e016df64e174984a2ef8f37c4b9[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 14 16:33:25 2013 +0200

    Issue #39 - Copy s screen table not work correctly fixed

[33mcommit 5344065bd39078ce661bc84de8ca0f99492897e0[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 14 16:22:46 2013 +0200

    Revision 6.6.2-SNAPSHOP

[33mcommit b24facd3c363700949a8d52bdce211bc5f06b203[m
Author: val100 <valery@agrologic.com>
Date:   Thu Nov 14 16:17:25 2013 +0200

    Issue #38 - Move all DerbyDaoImpl classes under SpringJdbc fixed

[33mcommit e938215e7f2110e77e9078dd580a7b72dda7f084[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 12 13:13:05 2013 +0200

    #37 Issue fixed

[33mcommit 233514238f259159705a75a454c5073c823fb39c[m
Author: val100 <valery@agrologic.com>
Date:   Sun Nov 10 11:52:06 2013 +0200

     #35 Issue fixed there is two exception was added DatabaseNotFound and WrongDatabaseException

[33mcommit c9e3bbf71c84d00b275b74ab9512edf6065a8363[m
Author: val100 <valery@agrologic.com>
Date:   Sun Nov 10 11:41:41 2013 +0200

    - refactored

[33mcommit 58cbddb8a967adae39d9911989e3ab0fa6117c4c[m
Author: val100 <valery@agrologic.com>
Date:   Sun Nov 10 11:40:46 2013 +0200

    - refactored all methods moved all under SpringJdbc

[33mcommit b4002a64d7e5432912d4fc3d2736feff6622ff7c[m
Author: val100 <valery@agrologic.com>
Date:   Sun Nov 10 11:28:57 2013 +0200

    - refactored all methods moved all under SpringJdbc
    - documented code

[33mcommit fc15ebc16986a9062bd41926f1cc27cb55ff9756[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 5 14:55:39 2013 +0200

     Removed unused methods

[33mcommit 57e9aa8fd9e75c11874ac3eba26bf7ae7025bac7[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 5 14:53:07 2013 +0200

    Organized imports

[33mcommit c0d0695b8a6676deab6f003aa1146d50aedab2b5[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 5 14:51:46 2013 +0200

    #33 issue fixed

[33mcommit 5d13f3b3d9c6e7e1fab20f35e884fe96a4ed1b2d[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 5 14:47:13 2013 +0200

     Removed unused and refactored

[33mcommit 9f2a5b698cab60b9a1ea2a329b9a9f1790a7497b[m
Author: val100 <valery@agrologic.com>
Date:   Tue Nov 5 14:43:36 2013 +0200

     #34 Issue fixed

[33mcommit 611a046e7ae40201aabcd4356997e84ec85eda0f[m
Author: val100 <valery@agrologic.com>
Date:   Sun Nov 3 10:52:55 2013 +0200

     fixed issue graph shows shifted series data on time line

[33mcommit 2ed0c8566ccc7678d7a9082b3db9a1fe5f64e0ef[m
Author: val100 <valery@agrologic.com>
Date:   Thu Oct 31 16:22:03 2013 +0200

     - added sorter to all-screens.jsp

[33mcommit 27b9ea6a4989457dba816682ac2cf75f0ea0c42f[m
Author: val100 <valery@agrologic.com>
Date:   Thu Oct 31 13:07:19 2013 +0200

     - fixed bug which cause creating database incorrectly

[33mcommit 974ca18ccb133b041f9d6f81f683fa3ca587cd74[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 16:11:58 2013 +0200

     - refactored edit controller added auto complete

[33mcommit 08505bb09f1e4f1a5a673fa74f85e30316de54a3[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:24:06 2013 +0200

     - refactored

[33mcommit 6d4a66f58e2e6dea51a30d4b47cad31eeb73d9c8[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:23:18 2013 +0200

     - refactored logger messages

[33mcommit 39cf3f4a343002ed9e1a04bd9b698fa31c1304fc[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:22:32 2013 +0200

     - refactored

[33mcommit 048e3c2f84f3a0f296914cdee3b44c0741f2d73d[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:22:16 2013 +0200

     - fixed bug appeared when data was changed in real time pages was not changed

[33mcommit dac1b3a4ed17d52dcd81235f81c9ca28c5022256[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:18:25 2013 +0200

     - added auto complete to controller types

[33mcommit 08f6308705bb944ee066330770367d1e863a2f9a[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:16:00 2013 +0200

     - refactored loading data ,  that reduce the time of loading database that used for creation derby database
     - removed unused code

[33mcommit 52e2c744583210f5845a8343efd792839af5b0db[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:14:36 2013 +0200

     - refactored loading data ,  that reduce the time of loading database that used for creation derby database

[33mcommit ff083d81d8a0cf50fde1d6bacd3d1d10637971aa[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 30 11:08:26 2013 +0200

     - refactoring

[33mcommit 1a1362dd54cd114b2f768c32b4b0f1c49614c9ee[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 29 16:25:11 2013 +0200

     - added auto complete to add program

[33mcommit b0a75edef2055b41539c70cb3107f055e2fea928[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 29 10:16:37 2013 +0200

     - disabled sorter where it was not used

[33mcommit 74cc7334c0632e7ea0355cc39804532f1b48c9af[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 28 17:04:22 2013 +0200

     - added auto complete programs in add controller page

[33mcommit 8f306d51fe45c5ad4536ff251756050cfda241a0[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 28 16:21:41 2013 +0200

     - added auto complete to controller types

[33mcommit e5926f1b8d4910aeff7e0aa0dbac88bf3fbd59f2[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 28 16:20:22 2013 +0200

     - refactoring add controller module . now net name sets by controller type

[33mcommit c4e7f3407ea94dff3e10874bee5676a34af5d9b7[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 23 16:54:04 2013 +0300

     - refactoring code
     - fixed bug in flock manager module
     - fixed bug in history graphs module
     - fixed bug in showing history table and exporting to excel
     - changed design in several pages
     - added using jquery
     - removed unused files

[33mcommit 0dc93aba4f4aa60ebbe90ac7b73c0e7c9e9d40b5[m
Author: val100 <valery@agrologic.com>
Date:   Wed Oct 23 16:52:22 2013 +0300

     - refactoring code
     - fixed bug in flock manager module
     - fixed bug in history graphs module
     - fixed bug in showing history table and exporting to excel
     - changed design in several pages
     - added using jquery
     - removed unused files

[33mcommit 073ff33c3584d4d69e70025acb99b9a210614dc2[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 15:27:06 2013 +0300

     - removed unused

[33mcommit 50841b478f20f846c8b197d30e666baeeada4dc5[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 15:14:25 2013 +0300

     - refactored code

[33mcommit 7a5a0d879c8ef234c06da04cab6c9ac82473f57e[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 14:01:17 2013 +0300

     - removed unused

[33mcommit f909ee9e58a7f7d8ac04995b06faf1439684b2eb[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:59:11 2013 +0300

     - moved all js , css and img into separate folder resources/javascript, resources/style, resource/images
     - added jquery files .
     - added using jquery methods for sorting tables and searching .
     - removed unused files
     - moved paginate into separate jsp with using tag libs  in it
     - moved message representing into separate jsp with using tag libs in it
     - added auto complete for searching data id  in add data page

[33mcommit dc7565e38fb5e4960c4486ffdaa2872e003d94ec[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:50:07 2013 +0300

     - removed unused and refactored some methods

[33mcommit d7ef1d8239c4789938c66e350c7ecc45a9699ed4[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:49:04 2013 +0300

     - added two classes that contain the history data ids and strings for daily history and per hour history

[33mcommit b1d7d0207fdac1cf37993e4e903ba768949b0cdf[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:46:54 2013 +0300

    -  UserDao changed to private

[33mcommit c826043956ee996d7fcbdba9f92d332daa64c10e[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:45:54 2013 +0300

    - 'FlockHistoryService' that responsible for creating and preparing history data

[33mcommit b11a861bb1d6194de669f84f6e6a4348cf726843[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 13:42:10 2013 +0300

     - removed unused
     - refactored several classes regarding history data , export to excel data , and representing to table history data
     - replaced all "session.setAttribute("object", object)"  occurrence  to "request.setAttribute("object", object)"
     - all 'Servlets' now inherited from 'AbstractServlet'

[33mcommit 160ec0a2fb72f0d5da187a6767b233eac5d79525[m
Author: val100 <valery@agrologic.com>
Date:   Tue Oct 15 10:20:06 2013 +0300

     - added auto complete

[33mcommit 7f14cfd26dc0310c31a46ceec2f8cd718f196548[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 14 16:49:59 2013 +0300

     - parent servlet for all servlet in system

[33mcommit 86e322d16f1a02344d49759e9a0f56617c919240[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 14 16:48:42 2013 +0300

     - added count with criteria
     - using jQuery to filter and search

[33mcommit f33dd6a28906e7474b20ef88e32647390cf763fb[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 14 16:14:50 2013 +0300

     - added count with criteria
      - using jQuery to search

[33mcommit 87e15617901f8464230d02c62743d5cfad7d23a7[m
Author: val100 <valery@agrologic.com>
Date:   Mon Oct 14 16:07:42 2013 +0300

     - added count with criteria
     - sorting company
     - refactor getAll(Integer role, String company, String search, String index) added paging index
     - using jQuery to filter and search

[33mcommit df3ae780612c8478961cc880a35c3f6c5b48788a[m
Author: val100 <valery@agrologic.com>
Date:   Mon Sep 30 17:14:49 2013 +0300

     - refactoring replaced scriplets to jstl tags

[33mcommit 813ab9b44a037205af611c8c38520f788365c8a1[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 15:01:51 2013 +0300

    refactoring :
     - changed log4j to slf4j
     - removed threads because of duplicated sessions that caused bug that occurs when server runs long period ( a hour and more )
     - moved class outer .

[33mcommit 9c33680797f33cd06897a080cb2e7fd5ecfb6cd8[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 14:59:59 2013 +0300

    refactoring :
     - changed log4j to slf4j
     - removed threads because of duplicated sessions that caused bug that occurs when server runs long period ( a hour and more )
     - moved class outer .

[33mcommit bb5b5c282cdb2a5ff9ba6c171af3087cb4b22d7e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 14:52:40 2013 +0300

    -revision
    -

[33mcommit d85daacbf133927fa56cb59182c13ef7c590d9cb[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 14:51:17 2013 +0300

    refactoring  closing session in finally block . The socket was closed aslo the current running session when duplicate session was appeared .

[33mcommit 868e2ec1b84b6a349d67ce456b4fb8cc50531aa2[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 14:46:57 2013 +0300

    Moved to separate class the monitoring and changing session state to starting communication state . This  will decrease significantly the amount of queries to the database .

[33mcommit ce2c07a77db89695fbd32fa2fb4bb8f3d5dbfd74[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 29 14:38:22 2013 +0300

    - the monitoring sessions size was moved out .

[33mcommit 563cd1fc0a0f405ebe95210e298d9426cf9a279b[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:49:26 2013 +0300

     - refactoring

[33mcommit fa7db7061279ccd6a4be1a43dfa698f5af508aab[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:48:36 2013 +0300

     - refactoring

[33mcommit 9f78925440cd1e9af098d8b4b881199752166ccc[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:47:45 2013 +0300

     - removed field 'Configuration'

[33mcommit ca67f00ee231deddbd9762c1654b4cc36b3db406[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:45:55 2013 +0300

     - removed unused Exception classes and renamed package

[33mcommit e4632d6c8581d31af820183e80f915c6fdba20e2[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:44:50 2013 +0300

     - removed unused

[33mcommit 22c7eec3974779cc9e12de228468060e681fab26[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:43:34 2013 +0300

     - added addition clearinputstream because of synchronization message index error

[33mcommit 3848db2d649884044e1e569bda9d9d04a82004ea[m
Author: val100 <valery@agrologic.com>
Date:   Wed Sep 18 11:41:37 2013 +0300

     - synchronized method getSessions
     - removed unused fields in 'ServerInfo'
     - 'ClientSessions' should be static
     - added 'MonitorSocketThreadSize'

[33mcommit 9244848c94f6210ccfa9f34be7dad658a6a5a9ee[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 16:52:34 2013 +0300

    refactoring creating executor from constructor 'ServerThread' into the  'startServer()'

[33mcommit 07c2bac9c5cd70e693fdf27dabc41179cf4959f2[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 16:48:10 2013 +0300

    moved socketthread monitoring and starting into the single thread

[33mcommit 079075c5b72db39855ea713804ea02523c179b95[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 10:53:08 2013 +0300

    removed unnecessary occurrences of  'response.setContentType("text/html;charset=UTF-8")'

[33mcommit e5e213cfbeec645e706dfe41470a254162a9a905[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 10:30:33 2013 +0300

    - refactored 'request.getRequestDispatcher("./login.jsp").forward(request, response);' to 'response.sendRedirect("./login.jsp");"

[33mcommit a3f7a0095d23156317629704c83cc06f00331da2[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 10:22:32 2013 +0300

    removed unnecessary  field serialVersionUID

[33mcommit 02f2712cb4107e5cf25e80b426acde00d9c3f59b[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 10:05:10 2013 +0300

     - moved polling cellink states to single thread that responsable to do that

[33mcommit 2fe64c31300c86d74d4ae7416b1dfbce45524aec[m
Author: val100 <valery@agrologic.com>
Date:   Tue Sep 17 09:55:22 2013 +0300

     - moved the whole FlockDao , RelayDao under SpringJdbc #21
     - added flockdao test

[33mcommit 73a33ca2af1c25ee642e5ebe2479b8ec164494e4[m
Author: val100 <valery@agrologic.com>
Date:   Mon Sep 16 12:16:20 2013 +0300

    -revision project
    -refactoring using Configuration class

[33mcommit 1561cf7c7bf450d56fd010b8a10069bb00324e9c[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 14:53:55 2013 +0300

    -added test for request from server
    -excluded FakeMessageSystemTest from maven

[33mcommit 62595fe80086990c36425804a0fd80d0ae8d2e3f[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 13:44:47 2013 +0300

    added schema for the database

[33mcommit 84576d9e392c375eafdddf982097c8e9d0779207[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 13:22:14 2013 +0300

    Fixed dao tests by specifying another relative path of where the database is located.

[33mcommit cf952c15577fff0d24e00a61157233b4064f94a8[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 12:48:11 2013 +0300

    #10
    - added executor threadpool insteadof creating threads manually
    - closing session after thread is finished

[33mcommit 4bb0ca404e990cb075ba06452bdfc9fcb9faa4e5[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 11:33:19 2013 +0300

      - moved the whole SystemStateDao , RelayDao, ProgramSystemStateDao, ProgramRelayDao under SpringJdbc #21
      - changed version
      - added program version number and name to main screen
      - replaced Null checks  with Validate.notNull() #8
      - removed unused code and imports
      - documented dao interfaces

[33mcommit f4dadf64e880b36b19d71e8d69e57eba07e5a831[m
Author: val100 <valery@agrologic.com>
Date:   Sun Sep 15 11:28:30 2013 +0300

      - moved the whole SystemStateDao , RelayDao, ProgramSystemStateDao, ProgramRelayDao under SpringJdbc #21
      - changed version
      - added program version number and name to main screen
      - replaced Null checks  with Validate.notNull() #8
      - removed unused code and imports
      - documented dao interfaces

[33mcommit a15ac500c60c58f9337900fa44ab7f29f33831bb[m
Author: val100 <valery@agrologic.com>
Date:   Wed Jul 31 11:21:27 2013 +0300

      - moved the whole UserDaoImpl, CellinkDaoImpl,ControllerDaoImpl, ProgramDaoImpl under SpringJdbc #21

[33mcommit 0749ba1100f25c3c18b1f9d2860d3ae313b59e6b[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jul 23 11:55:19 2013 +0300

      - moved the whole UserDao under SpringJdbc #21
      - changed version
      - documented dao interfaces

[33mcommit b46a3f86df94cbac59deb0ffbc596d1145dac6c2[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jul 15 16:37:31 2013 +0300

      - UserDao test added

[33mcommit 86d160aa6872279f598f445d4dd1564012610d1b[m
Author: val100 <valery@agrologic.com>
Date:   Wed Jul 3 11:29:25 2013 +0300

     - added 'alarm.gif' to put on house name button

[33mcommit 3c5bed7c91a398c4f322602074c2eb157a8d35d9[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jul 2 16:26:20 2013 +0300

      'lighton.gif' was added to resources\images

[33mcommit be12b8c5e8a06ddb85d0ac8be59b8e2940982fc2[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jul 2 09:13:03 2013 +0300

      - moved the whole ProgramDao , ScreenDao, TableDao, DataDao under Spring AOP without SpringJdbc #21

[33mcommit 026a7e53475c6ab3063f56e8146469f87b90f6ff[m
Author: val100 <valery@agrologic.com>
Date:   Sun Jun 23 11:54:47 2013 +0300

      - added unit test to RequestMesssageQueue #6

[33mcommit ae1c9455780bd13ae73b6c5df281ad6360f845f2[m
Author: val100 <valery@agrologic.com>
Date:   Sun Jun 23 11:05:28 2013 +0300

      - Added unit test to RequestMesssageQueue

[33mcommit f8f4ba6458e803527277ba38a87efeca89afddd2[m
Author: val100 <valery@agrologic.com>
Date:   Thu Jun 20 16:17:29 2013 +0300

    - moved the whole SystemStateDao , RelayDao, ProgramSystemStateDao, ProgramRelayDao under SpringJdbc #21

[33mcommit c6e10cce53cbc4651600dc067b013c8cbad6f2e3[m
Author: val100 <valery@agrologic.com>
Date:   Tue Jun 18 09:44:10 2013 +0300

    Continue to moved the whole AlarmDao and ProgramAlarmDao under SpringJdbc #20

[33mcommit b92e491bcf325f63fe06db7ed9db682488733cec[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 10 16:37:09 2013 +0300

     - Moved the whole AlarmDao under SpringJdbc #20
     - Changed version

[33mcommit 368c77b4854c641bfddb3e3888bec1d2df4607f5[m
Author: val100 <valery@agrologic.com>
Date:   Mon Jun 10 16:19:28 2013 +0300

    Moved the whole AlarmDao under SpringJdbc #20

[33mcommit 4e6771f822be86e4da4016cc5e5b9a668d8c0528[m
Author: val100 <valery@agrologic.com>
Date:   Sun Jun 2 11:56:19 2013 +0300

    Fixed synchronization between indexes of request and response messages error #27

[33mcommit ae0a416e2cf3cf0988196200efc32335b91c95b5[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 30 16:11:47 2013 +0300

    Replaced "javax.swing.Timer" in "CellinkTable" with "Executors.newSingleThreadScheduledExecutor() #15"

[33mcommit e72e714dc6c231b216244389d04b52e4d18f2e1d[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 30 15:44:54 2013 +0300

    Replaced javax.swing.Timer and TimerTask in CellinkTable Executors.newSingleThreadScheduledExecutor() #15

[33mcommit 543cf9c2c4c029c460c6930b23dc286a24f7f890[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 29 17:04:00 2013 +0300

    Using "java.util.concurrent.TimeUnit" instead of CommonConstant #26

[33mcommit 8edf8e2a37629d6baa1be974b52f404ec22b21b3[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 29 16:57:43 2013 +0300

    Refactoring several classes according to issue #25

[33mcommit 5a683e45b70b997121fa5d9b445075961d70bb54[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 29 11:23:17 2013 +0300

    Moved the logic of internalization in a separate package and class #25
       * added a i18n package and LocaleManager support in the webchick-server-main module
       * moved all the hardcoded strings/messages in the created resource bundle
       * LocaleManager covered with test

[33mcommit 54b5e1584b0690702dd3f447641ed37671ec5082[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 23 09:48:20 2013 +0300

    fixed issue that caused not to send request for changing data in controller

[33mcommit 0530743ade74f7b4345f1e52e12b1f3917af861b[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 23 09:36:51 2013 +0300

    * removed unused abstract methods fromDaoFactory
    * moved Utils that used for wrapping database results to dao package

[33mcommit 85cde9280d5dc1a0dde69d1d7d8be58f9197c42e[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 10:53:44 2013 +0300

    * refactoring exception package
    * removed unused classes
    * added Util class to RestartApplication and partly covered with test

[33mcommit 3b5b83ce85fa3a7b8e877385b166d6324206b3be[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 10:27:39 2013 +0300

    * removed unused images
    * renamed images

[33mcommit fb5c21748bc65b966b1cb5babeffa146c2ede666[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 10:23:46 2013 +0300

    * optimize imports according to previous changes

[33mcommit e107059173376a4c19761adb40984c67cb92253a[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 10:20:19 2013 +0300

    * removed unused abstract methods fromDaoFactory
    * moved Utils that used to map row of data for each table in database

[33mcommit 664c97c31880f69bb3cef2ca748f215fd304d44f[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 09:57:14 2013 +0300

    refactoring imports according to changes ind webchick-domain module .

[33mcommit 83c2dd2e98b0726935805d4a2345c3e534c583eb[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 22 09:55:11 2013 +0300

    reformat source code

[33mcommit 28a131d8c23dee5583c439d1035bd63f066e6946[m
Author: val100 <valery@agrologic.com>
Date:   Tue May 21 10:57:02 2013 +0300

    * Removed extra daFactory

[33mcommit c52af8431e08c61540558bde1b6cf72177beb40d[m
Author: val100 <valery@agrologic.com>
Date:   Tue May 21 08:56:58 2013 +0300

    ResponseMessage recovered with test #7

[33mcommit f769b4985d90435ead3b0027ee8ba70ac8903684[m
Author: val100 <valery@agrologic.com>
Date:   Sun May 19 15:39:56 2013 +0300

    * Moved out props to properties
    * getById() was rewritten according to SpringJdbc

[33mcommit ba66aa03fcea284317e7524ad5f2137b1e5a695a[m
Author: val100 <valery@agrologic.com>
Date:   Sun May 19 13:35:45 2013 +0300

    * Configured a C3P0 db pool
    * Started to use Spring Tx, Spring AOP
    * Moved to JdbcTemplate instead of pure JDBC

[33mcommit 98fa200679616a85a194cf4ee08201aae1d683ec[m
Author: val100 <valery@agrologic.com>
Date:   Sun May 19 10:05:38 2013 +0300

    * Removed unnecessary methods from DaoFactories
    * Checked location of images

[33mcommit 582bbd886916c2937170966d1a7a562ec61af22d[m
Author: val100 <valery@agrologic.com>
Date:   Fri May 17 10:32:37 2013 +0300

    ResponseMessage covered with test .

[33mcommit 0350a81ed70fe2e3ee3c688cec8512e2abb21793[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 16 08:42:10 2013 +0300

    removed unused constants

[33mcommit 4670fce81426145ee548edd9b2ae93bba072c9b8[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 16 08:39:02 2013 +0300

    * move the logic parsing of data in a separate class MessageParser

[33mcommit 31df7f283647c50548a038c61b864b4ab4d6fbfc[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 16 08:29:23 2013 +0300

    refactoring all remaining methods that uses  DbImplDecider.use(DaoType.MYSQL) instead of     DbImplDecider.use(DaoType.DERBY)

[33mcommit 47a1253381b9380cfd463db25711034cffed338b[m
Author: val100 <valery@agrologic.com>
Date:   Sun May 5 15:45:37 2013 +0300

    changing class with constants to interface

[33mcommit c89a89d54e646478b67ed7288ab11db316f4eb0d[m
Author: val100 <valery@agrologic.com>
Date:   Sun May 5 15:37:17 2013 +0300

    added 'common' package and 'CommonConstant' in order to isolate the common constant variables (second, minute, hour , e.g.)  #14

[33mcommit db5e39aa8b96b7f43a49bd589249f194be3dcfa8[m
Author: val100 <valery@agrologic.com>
Date:   Thu May 2 13:49:32 2013 +0300

    * refactoring all classes that used 'DbImplDecider.getDaoFactory(DaoType)' method
    * added 'VersionDao' to get version of database

[33mcommit 6bdace02eaa51eb0b7e6abe6c3a7231c97e7a5cd[m
Author: val100 <valery@agrologic.com>
Date:   Wed May 1 17:17:17 2013 +0300

    deleted unused code and comments  in webchick-server #13

[33mcommit 806c00132573b23c4cf99ea9a6a3a6e1e4a09efe[m
Author: val100 <valery@agrologic.com>
Date:   Tue Apr 30 14:50:41 2013 +0300

    document RequestMessageQueue methods

[33mcommit 507ce07a1b1ff33906fd8d5c6b37a0316469ff43[m
Author: val100 <valery@agrologic.com>
Date:   Mon Apr 29 17:08:36 2013 +0300

    document MessageFactory methods #5

[33mcommit 425e225fa75bc10cf8e0fdae8a873f1781fa1c42[m
Merge: 6de4a96 9eb4a27
Author: val100 <valery@agrologic.com>
Date:   Mon Apr 29 13:02:07 2013 +0300

    Merge remote-tracking branch 'origin/master'

[33mcommit 6de4a965860f0d8801133d707b5ec8fdc64341ea[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Sun Apr 28 16:26:28 2013 +0300

    Moved all DAOs under spring issue #4

[33mcommit 9eb4a27dfcb6405243a152d42f85025e7f3d2ea1[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 16:26:28 2013 +0300

    Refactored RequestMessageQueue:
    * Got rid of extra Thread.sleep()
    * Removed duplication

[33mcommit 57308d56e68f32438421aea814a1663cd9bc1f57[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 15:08:16 2013 +0300

    Moved out all RequestMessage creation into MessageFactory

[33mcommit 8e5ae3ea07d099794ed0539de06cdd846115d85d[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 14:43:44 2013 +0300

    * introduced MessageFactory to delegate message request creation
    * removed DNUM constants and created an enum with human-readable names

[33mcommit 85666ed44a25b1557dcb718d331523905e979c2f[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 10:05:09 2013 +0300

    added dao-contexts for both mysql and derby

[33mcommit 0a2ff47dad6850c04f6325e2a76e205e1091cda0[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 09:12:09 2013 +0300

    moved in webchick web

[33mcommit 4b24ffe8ac1ddf79775f8c58055c3768c5a573f4[m
Merge: 747c4dc 92b6cfe
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 08:38:49 2013 +0300

    Merge branch 'refactoring'

[33mcommit 92b6cfe592ba2c5b20a107e981097be223235d51[m[33m ([m[1;31morigin/refactoring[m[33m, [m[1;32mrefactoring[m[33m)[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 08:24:47 2013 +0300

    added agrodb to gitignore

[33mcommit 0678f48c471e3651941aaf02db1a2858543642cc[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 28 08:19:25 2013 +0300

    small refactoring (mostly removing extra methods)

[33mcommit a241ce4dd130a8879f79f1ffbb874f30acfd7f4c[m
Author: val100 <valery@agrologic.com>
Date:   Mon Apr 22 16:44:10 2013 +0300

    added status panel to main window

[33mcommit 107a55e3b3905d50d46f6a8acf9ee7589d3b7c31[m
Author: val100 <valery@agrologic.com>
Date:   Mon Apr 22 13:41:37 2013 +0300

    refactoring message manager  , data was not updated on gui .

[33mcommit 05a30f7f6b32c4f687e75118a9617c748a0193f7[m
Author: val100 <valery@agrologic.com>
Date:   Mon Apr 22 09:33:23 2013 +0300

    fixed Util class

[33mcommit 3ac3c2a19ff24448f9de1aba4a3b89028754511d[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 15:59:54 2013 +0300

    moved resources to respective folder

[33mcommit a4bf8b0684c42754a79da8e513d720ed1302252e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 15:41:02 2013 +0300

    moved derby daos to respective module

[33mcommit 722390d64abd6849824344ef3697cb645892d028[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 15:30:38 2013 +0300

    removed extra MySqlDaoFactory

[33mcommit 01258c87c6488673a94063fd2b3b300d4f4a4c70[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 15:25:31 2013 +0300

    Moved dao factory to domain

[33mcommit dbfee34c6d49f6d2d4719e578d2953946767078f[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 15:17:42 2013 +0300

    * Moved all mysql dao classes into a separate module
    * Created a DbImplDecider to decouple DAOs from particular database

[33mcommit 4e966a8c37d49923749156796ad934cd6cf2b05e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 12:48:56 2013 +0300

    * Continued moving dao classes to new module

[33mcommit 8cea3278260c758166f8d89f41e0b9e172b6b2dc[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 12:32:28 2013 +0300

    * Moved AlarmDao & ControllerDao to new module
    * Refactored ControllerDao not to use MessageManager

[33mcommit aaf9403ae382a9c11af39491295c78d0ef18ad80[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 12:09:08 2013 +0300

    moved assembly plugin to webchick-server-main. now in order to get a runnable jar:
    * mvn clean install
    * mvn assembly:single -pl webchick-server-main

[33mcommit 67197cacac6ae1bfb9499d48dac01c5b47070791[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 11:42:10 2013 +0300

    removed sources from pom module

[33mcommit 6cd2594b0670b8d566e7c81826b1d38de16d4c87[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 11:40:42 2013 +0300

    created a module webchick-server-main

[33mcommit ed4b7f116847bb94ab8ac49799c773dcb9fa0a2c[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 11:33:02 2013 +0300

    moved domain classes from server to client-ui

[33mcommit 43e0bb89e34275347379a06a19393fc4fca1c268[m
Merge: 7181bd0 9bab131
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 10:34:16 2013 +0300

    Merge remote-tracking branch 'origin/refactoring' into refactoring

[33mcommit 7181bd099f0e64e930dbb063f1b0f0375e02d68e[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 21 10:33:31 2013 +0300

    Moved logic of keep alive parsing out from SocketThread

[33mcommit 9bab131b33c16ee0814e0ad60888e227e88db04a[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Thu Apr 18 13:07:46 2013 +0300

    added first assessment and desired changes
    
    - assessment of the project in the initial development stage .
    - list of desired changes .

[33mcommit 747c4dcdbf482a9ad7a48180f62a70084a5b39a8[m
Author: Valery Manakhimov <valcomman@gmail.com>
Date:   Thu Apr 18 13:05:46 2013 +0300

     added first assessment and desired changes
    
    - assessment of the project in the initial development stage .
    - list of desired changes .

[33mcommit e70205e94a30e3edacf2f8af396429e0b8fa5334[m
Author: val100 <valery@agrologic.com>
Date:   Thu Apr 18 12:50:32 2013 +0300

    refactoring keep alive message

[33mcommit 495390de2ba7c2348cb319063d16802b8eff7844[m
Author: val100 <valery@agrologic.com>
Date:   Wed Apr 17 11:13:14 2013 +0300

    Moved keep alive logic int KeepAliveMessage class and added test

[33mcommit 99004b796811764e6089d38eba7fa529ef9c6dc9[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 16:45:27 2013 +0300

    Moved logic of keep alive parsing out from SocketThread

[33mcommit de43502bc8626c0c472fd8d35ac76f4fdf074125[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 16:05:26 2013 +0300

    * Configured SLF4J
    * Refactored SocketThread to use ClientSessions

[33mcommit 04585666cbb23e05e21e414b59489c1ee9111aa0[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 14:47:51 2013 +0300

    * Ignored failing tests that are env-depending
    * Now tests are run in maven

[33mcommit b90261d10c261ee62f564bf38f18e298941d83bf[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 14:42:10 2013 +0300

    ClientSessions was introduced to control connections

[33mcommit 8f10162b2ec586984d8025dba4baf9e20d17f565[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 11:56:05 2013 +0300

    moved resources package to maven resources

[33mcommit 7243dd7aeaface7d1d6be150be2a693cd0c3448b[m
Author: val100 <valery@agrologic.com>
Date:   Sun Apr 14 11:39:34 2013 +0300

    initial commit after we have removed all libs and mavenized the project
