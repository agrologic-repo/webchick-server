package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.*;
import com.agrologic.app.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

public class RowMappers {

    protected final static Logger logger = LoggerFactory.getLogger(RowMappers.class);


    public static LanguageMapper language() {
        return new LanguageMapper();
    }

    public static RelayMapper relay() {
        return new RelayMapper();
    }

    public static ProgramRelayMapper programRelay() {
        return new ProgramRelayMapper();
    }

    public static AlarmMapper alarm() {
        return new AlarmMapper();
    }

    public static ProgramAlarmMapper programAlarm() {
        return new ProgramAlarmMapper();
    }

    public static SystemStateMapper systemState() {
        return new SystemStateMapper();
    }

    public static ProgramSystemStateMapper programSystemState() {
        return new ProgramSystemStateMapper();
    }

    public static ProgramMapper program() {
        return new ProgramMapper();
    }

    public static ScreenMapper screen() {
        return new ScreenMapper();
    }

    public static TableMapper table() {
        return new TableMapper();
    }

    public static DataMapper data() {
        return new DataMapper();
    }

    public static ControllerMapper controller() {
        return new ControllerMapper();
    }

    public static CellinkMapper cellink() {
        return new CellinkMapper();
    }

    public static UserMapper user() {
        return new UserMapper();
    }

    public static FlockMapper flock() {
        return new FlockMapper();
    }

    public static DistribMapper distrib() {
        return new DistribMapper();
    }

    public static WorkerMapper worker() {
        return new WorkerMapper();
    }

    public static FeedMapper feed() {
        return new FeedMapper();
    }

    public static FeedTypeMapper feedType() {
        return new FeedTypeMapper();
    }

    public static FuelMapper fuel() {
        return new FuelMapper();
    }

    public static GasMapper gas() {
        return new GasMapper();
    }

    public static LaborMapper labor() {
        return new LaborMapper();
    }

    public static MedicineMapper medicine() {
        return new MedicineMapper();
    }

    public static SpreadMapper spread() {
        return new SpreadMapper();
    }

    public static TransactionMapper transaction() {
        return new TransactionMapper();
    }

    public static ActionSetMapper actionSet() {
        return new ActionSetMapper();
    }

    public static ProgramActionSetMapper programAcctionSet() {
        return new ProgramActionSetMapper();
    }

    public static EggsMapper eggs() {
        return new EggsMapper();
    }

    private static class LanguageMapper implements RowMapper<Language> {

        @Override
        public Language mapRow(ResultSet rs, int rowNum) throws SQLException {
            Language language = new Language();
            language.setId(rs.getLong("ID"));
            language.setLanguage(rs.getString("Lang"));
            language.setShortLang(rs.getString("Short"));
            return language;
        }
    }

    private static class RelayMapper implements RowMapper<Relay> {

        @Override
        public Relay mapRow(ResultSet rs, int rowNum) throws SQLException {
            Relay relay = new Relay();
            relay.setId(rs.getLong("ID"));
            relay.setText(rs.getString("Name"));
            try {
                relay.setLangId(rs.getLong("LangID"));
            } catch (SQLException ex) {
                // by default language  id is english
                relay.setLangId((long) 1);
            }

            try {
                relay.setUnicodeText(rs.getString("UnicodeText"));
            } catch (SQLException ex) {
                // if unicode for this relay does not exist
                // we take the text that was inserted for name
                relay.setUnicodeText(relay.getText());
            }

            return relay;
        }
    }

    private static class ProgramRelayMapper implements RowMapper<ProgramRelay> {

        @Override
        public ProgramRelay mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProgramRelay programRelay = new ProgramRelay();
            programRelay.setDataId(rs.getLong("DataID"));
            programRelay.setBitNumber(rs.getInt("BitNumber"));
            programRelay.setText(rs.getString("Text"));
            programRelay.setProgramId(rs.getLong("ProgramID"));
            programRelay.setRelayNumber(rs.getInt("RelayNumber"));
            programRelay.setRelayTextId(rs.getLong("RelayTextID"));
            try {
                if (rs.getString("UnicodeText") != null) {
                    programRelay.setUnicodeText(rs.getString("UnicodeText"));
                }

            } catch (SQLException ex) {
                programRelay.setUnicodeText(programRelay.getText());
            }
            return programRelay;

        }
    }

    private static class AlarmMapper implements RowMapper<Alarm> {

        @Override
        public Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
            Alarm alarm = new Alarm();
            alarm.setId(rs.getLong("ID"));
            alarm.setText(rs.getString("Name"));
            int count = rs.getMetaData().getColumnCount();
            if (count > 2) {
                alarm.setLangId(rs.getLong("LangID"));
                alarm.setUnicodeText(rs.getString("UnicodeName"));
            } else {
                alarm.setLangId((long) 1);
                alarm.setUnicodeText(alarm.getText());
            }
            return alarm;
        }
    }

    private static class ProgramAlarmMapper implements RowMapper<ProgramAlarm> {

        @Override
        public ProgramAlarm mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProgramAlarm programAlarm = new ProgramAlarm();
            programAlarm.setDataId(rs.getLong("DataID"));
            programAlarm.setDigitNumber(rs.getInt("DigitNumber"));
            programAlarm.setText(rs.getString("Text"));
            programAlarm.setProgramId(rs.getLong("ProgramID"));
            programAlarm.setAlarmTextId(rs.getLong("AlarmTextID"));
            try {
                if (rs.getString("UnicodeName") != null) {
                    programAlarm.setText(rs.getString("UnicodeName"));
                }
            } catch (SQLException ex) {    /*
                 * ignore
                 */
                programAlarm.setText(programAlarm.getText());
            }
            return programAlarm;
        }
    }

    private static class SystemStateMapper implements RowMapper<SystemState> {

        @Override
        public SystemState mapRow(ResultSet rs, int rowNum) throws SQLException {
            SystemState systemState = new SystemState();
            systemState.setId(rs.getLong("ID"));
            systemState.setText(rs.getString("Name"));
            try {
                systemState.setLangId(rs.getLong("LangID"));
            } catch (SQLException ex) {
                // by default language  id is english
                systemState.setLangId((long) 1);
            }

            try {
                systemState.setUnicodeText(rs.getString("UnicodeName"));
            } catch (SQLException ex) {
                systemState.setUnicodeText(systemState.getText());
            }

            return systemState;
        }
    }

    private static class ProgramSystemStateMapper implements RowMapper<ProgramSystemState> {

        @Override
        public ProgramSystemState mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProgramSystemState programSystemState = new ProgramSystemState();
            programSystemState.setDataId(rs.getLong("DataID"));
            programSystemState.setNumber(rs.getInt("Number"));
            programSystemState.setText(rs.getString("Text"));
            programSystemState.setProgramId(rs.getLong("ProgramID"));
            programSystemState.setSystemStateNumber(rs.getInt("SystemStateNumber"));
            programSystemState.setSystemStateTextId(rs.getLong("SystemStateTextID"));
            try {
                if (rs.getString("UnicodeName") != null) {
                    programSystemState.setText(rs.getString("UnicodeName"));
                }
            } catch (SQLException ex) {
                /**
                 * ignore
                 */
                programSystemState.setText(rs.getString("Text"));
            }
            return programSystemState;
        }
    }

    private static class ProgramMapper implements RowMapper<Program> {

        @Override
        public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
            Program program = new Program();
            program.setId(rs.getLong("ProgramID"));
            program.setName(rs.getString("Name"));
            program.setCreatedDate(rs.getString("Created"));
            program.setModifiedDate(rs.getString("Modified"));
            return program;
        }
    }

    private static class ScreenMapper implements RowMapper<Screen> {

        @Override
        public Screen mapRow(ResultSet rs, int rowNum) throws SQLException {
            Screen screen = new Screen();
            screen.setId(rs.getLong("ScreenID"));
            screen.setTitle(rs.getString("Title"));
            screen.setProgramId(rs.getLong("ProgramID"));
            screen.setDisplay(rs.getString("DisplayOnPage"));
            screen.setPosition(rs.getInt("Position"));
            screen.setDescript(rs.getString("Descript"));
            int count = rs.getMetaData().getColumnCount();
            if (count > 6) {
                screen.setLangId(rs.getLong("LangId"));
                if (rs.getString("UnicodeTitle") != null) {
                    screen.setUnicodeTitle(rs.getString("UnicodeTitle"));
                } else {
                    screen.setUnicodeTitle(rs.getString("Title"));
                }
            } else {
                screen.setLangId((long) 1);
                screen.setUnicodeTitle(rs.getString("Title"));
            }
            return screen;
        }
    }

    private static class TableMapper implements RowMapper<Table> {

        @Override
        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table table = new Table();
            table.setId(rs.getLong("TableID"));
            table.setTitle(rs.getString("Title"));
            table.setScreenId(rs.getLong("ScreenID"));
            table.setProgramId(rs.getLong("ProgramID"));
            table.setDisplay(rs.getString("DisplayOnScreen"));
            table.setPosition(rs.getInt("Position"));
            int count = rs.getMetaData().getColumnCount();
            if (count > 6) {
                table.setLangId(rs.getLong("LangId"));
                if (rs.getString("UnicodeTitle") != null) {
                    table.setUnicodeTitle(rs.getString("UnicodeTitle"));
                } else {
                    table.setUnicodeTitle(rs.getString("Title"));
                }
            } else {
                table.setLangId((long) 1);
                table.setUnicodeTitle(rs.getString("Title"));
            }
            return table;
        }
    }

    private static class DataMapper implements RowMapper<Data> {

        @Override
        public Data mapRow(ResultSet rs, int rowNum) throws SQLException {
            Locale locale = Locale.getDefault();
            Locale.setDefault(Locale.ENGLISH);

            Data data = new Data();


            HashMap dataMap = new HashMap();
            ResultSetMetaData metaData = rs.getMetaData();

            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                dataMap.put(metaData.getColumnName(i).toUpperCase(), rs.getObject(metaData.getColumnName(i)));
            }

            data.setId(rs.getLong("DataID".toUpperCase()));
            data.setType(rs.getLong("Type".toUpperCase()));
            data.setStatus(rs.getBoolean("Status".toUpperCase()));
            data.setReadonly(rs.getBoolean("ReadOnly".toUpperCase()));
            data.setTitle(rs.getString("Title".toUpperCase()));
            data.setFormat(rs.getInt("Format".toUpperCase()));
            data.setLabel(rs.getString("Label".toUpperCase()));
            data.setSpecial(rs.getInt("IsSpecial".toUpperCase()));
            data.setIsRelay(rs.getBoolean("IsRelay".toUpperCase()));

            // if position doesn't occur in result set ignore
            if (dataMap.get("Position".toUpperCase()) != null) {
                data.setPosition(rs.getInt("Position"));
            }
            // if position doesn't occur in result set ignore
            if (dataMap.get("DisplayOnTable".toUpperCase()) != null) {
                data.setDisplay(rs.getString("DisplayOnTable"));
            }

            if (dataMap.get("LangID".toUpperCase()) != null) {
                data.setLangId(rs.getLong("LangID"));
            }

            // if value doesn't occur in result set ignore
            if (dataMap.get("Value".toUpperCase()) != null) {
                data.setValue(rs.getLong("Value"));
            }

            // if unicode doesn't occur in result set ignore
            if (dataMap.get("UnicodeLabel".toUpperCase()) != null
                    && !dataMap.get("UnicodeLabel".toUpperCase()).equals("")) {
                data.setUnicodeLabel(rs.getString("UnicodeLabel"));
            } else {
                data.setUnicodeLabel(rs.getString("Label"));
            }

            if (dataMap.get("SpecialLabel".toUpperCase()) != null) {
                data.setUnicodeLabel(rs.getString("SpecialLabel"));
            }

            if (dataMap.get("HistoryOpt".toUpperCase()) != null) {
                data.setHistoryOpt(rs.getString("HistoryOpt"));
            }

            if (dataMap.get("HistoryDNum".toUpperCase()) != null) {
                data.setHistoryDNum(rs.getString("HistoryDNum"));
            }

            if (dataMap.get("HistoryData".toUpperCase()) != null) {
                data.setHistoryData(rs.getString("HistoryData"));
            }

            Locale.setDefault(locale);

            return data;
        }
    }

    private static class ControllerMapper implements RowMapper<Controller> {

        @Override
        public Controller mapRow(ResultSet rs, int rowNum) throws SQLException {
            Controller controller = new Controller();
            controller.setId(rs.getLong("ControllerID"));
            controller.setNetName(rs.getString("NetName"));
            controller.setTitle(rs.getString("Title"));
            controller.setName(rs.getString("ControllerName"));
            controller.setArea(rs.getInt("Area"));
            controller.setCellinkId(rs.getLong("CellinkId"));
            controller.setProgramId(rs.getLong("ProgramId"));
            controller.setActive(rs.getBoolean("Active"));

            try {
                //Compatibility to previous version 6.6.3
                controller.setHouseType(rs.getString("HouseType"));
            } catch (SQLException ex) {
                //
                controller.setHouseType("");
            }

            return controller;
        }
    }

    private static class CellinkMapper implements RowMapper<Cellink> {

        @Override
        public Cellink mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cellink cellink = new Cellink();
            cellink.setId(rs.getLong("CellinkID"));
            cellink.setName(rs.getString("Name"));
            cellink.setPassword(rs.getString("Password"));
            cellink.setTime(rs.getTimestamp("Time"));
            cellink.setIp(rs.getString("IP"));
            cellink.setPort(rs.getInt("Port"));
            cellink.setState(rs.getInt("State"));
            cellink.setScreenId(rs.getLong("ScreenID"));
            cellink.setSimNumber(rs.getString("SIM"));
            cellink.setUserId(rs.getLong("UserID"));
            cellink.setType(rs.getString("Type"));
            cellink.setVersion(rs.getString("Version"));
            cellink.setActual(rs.getBoolean("Actual"));
            cellink.setValidate(true);
            return cellink;
        }
    }

    private static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("UserID"));
            user.setFirstName(rs.getString("FirstName"));
            user.setLastName(rs.getString("LastName"));
            user.setLogin(rs.getString("Name"));
            user.setPassword(rs.getString("Password"));
            String decpsswd = new String(Base64.decode(rs.getString("Password")));
            user.setPassword(decpsswd);
            user.setState(rs.getInt("State"));
            user.setUserRole(rs.getInt("Role"));
            user.setPhone(rs.getString("Phone"));
            user.setEmail(rs.getString("Email"));
            user.setCompany(rs.getString("Company"));
            user.setValidate(true);
            return user;
        }
    }

    private static class FlockMapper implements RowMapper<Flock> {

        @Override
        public Flock mapRow(ResultSet rs, int rowNum) throws SQLException {
            Flock flock = new Flock();
            flock.setFlockId(rs.getLong("FlockID"));
            flock.setControllerId(rs.getLong("ControllerId"));
            flock.setFlockName(rs.getString("Name"));
            flock.setStatus(rs.getString("Status"));
            flock.setStartDate(rs.getString("StartDate"));
            flock.setEndDate(rs.getString("EndDate"));
            try {
                flock.setQuantityMale(rs.getInt("QuantityMale"));
            } catch (SQLException ex) {
                //
            }
            try {
                flock.setCostChickMale(rs.getFloat("CostChickMale"));
            } catch (SQLException ex) {
                //;
            }
            try {
                flock.setQuantityFemale(rs.getInt("QuantityFemale"));
            } catch (SQLException ex) {
                //;
            }
            try {
                flock.setCostChickFemale(rs.getFloat("CostChickFemale"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setQuantityFemale(rs.getInt("QuantityFemale"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostChickFemale(rs.getFloat("CostChickFemale"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalChicks(rs.getFloat("TotalChicks"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setGasBegin(rs.getInt("GasBegin"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setGasEnd(rs.getInt("GasEnd"));
            } catch (SQLException ex) {
                ;
            }
            try {

                flock.setCostGas(rs.getFloat("CostGas"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostGasEnd(rs.getFloat("CostGasEnd"));
            } catch (SQLException ex) {
                ;
            }

            try {
                flock.setGasAdd(rs.getInt("GasAdd"));
            } catch (SQLException ex) {
                ;
            }

            try {
                flock.setTotalGas(rs.getFloat("TotalGas"));
            } catch (SQLException ex) {
                ;
            }

            try {
                flock.setFuelBegin(rs.getInt("FuelBegin"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setFuelEnd(rs.getInt("FuelEnd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostFuel(rs.getFloat("CostFuel"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostFuelEnd(rs.getFloat("CostFuelEnd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setFuelAdd(rs.getInt("FuelAdd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalFuel(rs.getFloat("TotalFuel"));
            } catch (SQLException ex) {
                ;
            }

            try {
                flock.setWaterBegin(rs.getInt("WaterBegin"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setWaterEnd(rs.getInt("WaterEnd"));
            } catch (SQLException ex) {
                ;
            }

            try {
                flock.setFeedAdd(rs.getInt("FeedAdd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalFeed(rs.getFloat("TotalFeed"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostWater(rs.getFloat("CostWater"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setQuantityWater(rs.getInt("QuantityWater"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalWater(rs.getFloat("TotalWater"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setElectBegin(rs.getInt("ElectBegin"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setElectEnd(rs.getInt("ElectEnd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCostElect(rs.getFloat("CostElect"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setQuantityElect(rs.getInt("QuantityElect"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalElect(rs.getFloat("TotalElect"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setSpreadAdd(rs.getInt("SpreadAdd"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalSpread(rs.getFloat("TotalSpread"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalLabor(rs.getFloat("TotalLabor"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setTotalMedic(rs.getFloat("TotalMedic"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setExpenses(rs.getFloat("Expenses"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setRevenues(rs.getFloat("Revenues"));
            } catch (SQLException ex) {
                ;
            }
            try {
                flock.setCurrency(rs.getString("Currency"));
            } catch (SQLException ex) {
                ;
            }
            return flock;
        }
    }

    private static class WorkerMapper implements RowMapper<Worker> {

        @Override
        public Worker mapRow(ResultSet rs, int rowNum) throws SQLException {
            Worker worker = new Worker();
            worker.setId(rs.getLong("ID"));
            worker.setName(rs.getString("Name"));
            worker.setDefine(rs.getString("Define"));
            worker.setPhone(rs.getString("Phone"));
            worker.setHourCost(rs.getFloat("HourCost"));
            worker.setCellinkId(rs.getLong("CellinkID"));
            return worker;
        }
    }

    private static class FeedMapper implements RowMapper<Feed> {

        @Override
        public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
            Feed feed = new Feed();
            feed.setId(rs.getLong("ID"));
            feed.setFlockId(rs.getLong("FlockID"));
            feed.setType(rs.getLong("Type"));
            feed.setAmount(rs.getInt("Amount"));
            feed.setDate(rs.getString("Date"));
            feed.setNumberAccount(rs.getInt("AccountNumber"));
            feed.setTotal(rs.getFloat("Total"));
            return feed;
        }
    }

    private static class FeedTypeMapper implements RowMapper<FeedType> {

        @Override
        public FeedType mapRow(ResultSet rs, int rowNum) throws SQLException {
            FeedType feed = new FeedType();
            feed.setId(rs.getLong("ID"));
            feed.setCellinkId(rs.getLong("CellinkID"));
            feed.setFeedType(rs.getString("FeedType"));
            feed.setPrice(rs.getFloat("Price"));
            return feed;
        }
    }

    private static class FuelMapper implements RowMapper<Fuel> {

        @Override
        public Fuel mapRow(ResultSet rs, int rowNum) throws SQLException {
            Fuel fuel = new Fuel();
            fuel.setId(rs.getLong("ID"));
            fuel.setFlockId(rs.getLong("FlockID"));
            fuel.setAmount(rs.getInt("Amount"));
            fuel.setDate(rs.getString("Date"));
            fuel.setNumberAccount(rs.getInt("NumberAccount"));
            fuel.setPrice(rs.getFloat("Price"));
            fuel.setTotal(rs.getFloat("Total"));
            return fuel;
        }
    }

    private static class GasMapper implements RowMapper<Gas> {

        @Override
        public Gas mapRow(ResultSet rs, int rowNum) throws SQLException {
            Gas gas = new Gas();
            gas.setId(rs.getLong("ID"));
            gas.setFlockId(rs.getLong("FlockID"));
            gas.setAmount(rs.getInt("Amount"));
            gas.setDate(rs.getString("Date"));
            gas.setNumberAccount(rs.getInt("NumberAccount"));
            gas.setPrice(rs.getFloat("Price"));
            gas.setTotal(rs.getFloat("Total"));
            return gas;
        }
    }

    private static class LaborMapper implements RowMapper<Labor> {

        @Override
        public Labor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Labor labor = new Labor();
            labor.setId(rs.getLong("ID"));
            labor.setFlockId(rs.getLong("FlockID"));
            labor.setWorkerId(rs.getLong("WorkerID"));
            labor.setDate(rs.getString("Date"));
            labor.setHours(rs.getInt("Hours"));
            labor.setSalary(rs.getFloat("Salary"));
            return labor;
        }
    }

    private static class MedicineMapper implements RowMapper<Medicine> {

        @Override
        public Medicine mapRow(ResultSet rs, int rowNum) throws SQLException {
            Medicine medicine = new Medicine();
            medicine.setId(rs.getLong("ID"));
            medicine.setFlockId(rs.getLong("FlockID"));
            medicine.setAmount(rs.getInt("Amount"));
            medicine.setName(rs.getString("Name"));
            medicine.setPrice(rs.getFloat("Price"));
            medicine.setTotal(rs.getFloat("Total"));
            return medicine;
        }
    }

    private static class SpreadMapper implements RowMapper<Spread> {
        @Override
        public Spread mapRow(ResultSet rs, int rowNum) throws SQLException {
            Spread spread = new Spread();
            spread.setId(rs.getLong("ID"));
            spread.setFlockId(rs.getLong("FlockID"));
            spread.setAmount(rs.getInt("Amount"));
            spread.setDate(rs.getString("Date"));
            spread.setNumberAccount(rs.getInt("NumberAccount"));
            spread.setPrice(rs.getFloat("Price"));
            spread.setTotal(rs.getFloat("Total"));
            return spread;
        }
    }

    private static class TransactionMapper implements RowMapper<Transaction> {

        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getLong("ID"));
            transaction.setFlockId(rs.getLong("FlockID"));
            transaction.setName(rs.getString("Name"));
            transaction.setExpenses(rs.getFloat("Expenses"));
            transaction.setRevenues(rs.getFloat("Revenues"));
            return transaction;
        }
    }

    private static class DistribMapper implements RowMapper<Distrib> {

        @Override
        public Distrib mapRow(ResultSet rs, int rowNum) throws SQLException {
            Distrib destrib = new Distrib();
            destrib.setId(rs.getLong("ID"));
            destrib.setFlockId(rs.getLong("flockid"));
            destrib.setAccountNumber(rs.getInt("accountnumber"));
            destrib.setSex(rs.getString("sex"));
            destrib.setTarget(rs.getString("target"));
            destrib.setNumOfBirds(rs.getInt("numofbirds"));
            destrib.setWeight(rs.getInt("weight"));
            destrib.setQuantityA(rs.getInt("quantitya"));
            destrib.setQuantityB(rs.getInt("quantityb"));
            destrib.setQuantityC(rs.getInt("quantityc"));
            destrib.setBadVeterinary(rs.getInt("badveterinary"));
            destrib.setBadAnother(rs.getInt("badanother"));
            destrib.setQuantityA(rs.getInt("pricea"));
            destrib.setQuantityB(rs.getInt("priceb"));
            destrib.setQuantityC(rs.getInt("pricec"));
            destrib.setBadVeterinary(rs.getInt("agedistreb"));
            destrib.setBadAnother(rs.getInt("averageweight"));
            destrib.setDtA(rs.getString("dta"));
            destrib.setDtB(rs.getString("dtb"));
            destrib.setDtC(rs.getString("dtc"));
            destrib.setDtVeterinary(rs.getString("dtveterinary"));
            destrib.setDtAnother(rs.getString("dtanother"));
            destrib.setTotal(rs.getLong("calcsum"));
            destrib.setHandSum(rs.getLong("handsum"));
            destrib.setTotal(rs.getLong("total"));
            return destrib;
        }
    }

    private static class ActionSetMapper implements RowMapper<ActionSet> {

        @Override
        public ActionSet mapRow(ResultSet rs, int rowNum) throws SQLException {
            ActionSet actionSet = new ActionSet();
            actionSet.setValueId(rs.getLong("ValueID"));
            actionSet.setDataId(rs.getLong("DataID"));
            actionSet.setLabel(rs.getString("Label"));

            try {
                actionSet.setUnicodeText(rs.getString("UnicodeText"));
            } catch (SQLException e) {
                actionSet.setUnicodeText(rs.getString("Label"));
            }

            try {
                actionSet.setLangId(rs.getLong("LangId"));
            } catch (SQLException e) {
                actionSet.setLangId(1L);
            }

            return actionSet;
        }
    }

    private static class ProgramActionSetMapper implements RowMapper<ProgramActionSet> {

        @Override
        public ProgramActionSet mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProgramActionSet programActionSet = new ProgramActionSet();
            programActionSet.setValueId(rs.getLong("ValueID"));
            programActionSet.setDataId(rs.getLong("DataID"));
            programActionSet.setLabel(rs.getString("Label"));

            try {
                programActionSet.setUnicodeText(rs.getString("UnicodeText"));
            } catch (SQLException e) {
                programActionSet.setUnicodeText(rs.getString("Label"));
            }

            try {
                programActionSet.setScreenId(rs.getLong("ScreenId"));
            } catch (SQLException e) {
                // ignore
            }

            try {
                programActionSet.setProgramId(rs.getLong("ProgramId"));
            } catch (SQLException e) {

                // ignore
            }

            try {
                programActionSet.setPosition(rs.getInt("Position"));
            } catch (SQLException e) {

                // ignore
            }

            try {
                programActionSet.setDisplayOnPage(rs.getString("DisplayOnPage"));
            } catch (SQLException e) {

                // ignore
            }
            return programActionSet;
        }
    }

    private static class EggsMapper implements RowMapper<Eggs> {

        @Override
        public Eggs mapRow(ResultSet rs, int rowNum) throws SQLException {
            Eggs eggs = new Eggs();
            eggs.setFlockId(rs.getLong("FlockId"));
            eggs.setDay(rs.getInt("Day"));
            eggs.setSoftShelled(rs.getInt("SoftShelled"));
            eggs.setCracked(rs.getInt("Cracked"));
            eggs.setNumOfBirds(rs.getInt("NumOfBirds"));
            eggs.setEggQuantity(rs.getInt("EggsQuantity"));
            eggs.setFeedConsump(rs.getLong("FeedConsump"));
            eggs.setWaterConsump(rs.getLong("WaterConsump"));
            eggs.setDailyMortal(rs.getLong("DailyMortal"));
            return eggs;
        }
    }
}

