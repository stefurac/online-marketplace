package usecasesTest;

import org.junit.*;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.*;
import usecases.*;
import entities.*;

import static org.junit.Assert.*;

public class TradeManagerTest {
    private TradeManager tm;
    private Trade t1;
    private Trade t2;
    private Trade t11;

    @Before
    public void setup() {
        ArrayList<Integer> tradeManagerList = new ArrayList<Integer>();
        tradeManagerList.add(1);
        tradeManagerList.add(1);
        tradeManagerList.add(1);

        ArrayList<String> i1 = new ArrayList<>();
        i1.add("i1");
        i1.add("i2");

        ArrayList<String> i2 = new ArrayList<>();
        i2.add("i3");

        ArrayList<String> i3 = new ArrayList<>();
        i3.add("i4");

        this.t1 = new Trade("u1", "u2", i1);
        this.t2 = new Trade("u3", "u4", i2);
        this.t11 = new Trade("u1", "u2", i3);
        t1.setTradeId("t1");
        t2.setTradeId("t2");
        t11.setTradeId("t11");
        t1.setDuration(30);
        t2.setDuration(-1);
        t11.setDuration(-1);
        t1.setDateCreated(LocalDate.now());
        t2.setDateCreated(LocalDate.now().minusDays(30));
        t11.setDateCreated(LocalDate.now().minusDays(3));

        ArrayList<Trade> listTrade = new ArrayList<Trade>();
        listTrade.add(t1);
        listTrade.add(t2);
        listTrade.add(t11);

        this.tm = new TradeManager(tradeManagerList, listTrade);
    }

    @Test
    public void testGetTradeIdToTrade() {
        ArrayList<Integer> tradeManagerList = new ArrayList<Integer>();
        tradeManagerList.add(1);
        tradeManagerList.add(1);
        tradeManagerList.add(1);

        ArrayList<Trade> listTrade = new ArrayList<Trade>();
        listTrade.add(t1);
        listTrade.add(t2);

        TradeManager copytm = new TradeManager(tradeManagerList, listTrade);
        HashMap<String, Trade> testhm = new HashMap<>();
        testhm.put("t1", t1);
        testhm.put("t2", t2);
        assertEquals(copytm.getTradeIdToTrade(), testhm);
    }

    @Test
    public void testSetCancelledThreshold() {
        tm.setCancelledThreshold(10);
        assertEquals(tm.getCancelledThreshold(), 10);
    }

    @Test
    public void testSetWeeklyTradeLimit() {
        tm.setWeeklyTradeLimit(10);
        assertEquals(tm.getWeeklyTradeLimit(), 10);
    }

    @Test
    public void testSetBorrowDiff() {
        tm.setBorrowDiff(10);
        assertEquals(tm.getBorrowDiff(), 10);
    }

    @Test
    public void testAddTrade() {
        ArrayList<String> i3 = new ArrayList<>();
        i3.add("i10");
        Trade t3 = new Trade("u10", "u11", i3);
        t3.setTradeId("t3");
        tm.addTrade(t3.getTradeId(), t3);
        assert(tm.getTradeIdToTrade().containsKey("t3"));
        assert(tm.getTradeIdToTrade().containsValue(t3));
    }

    @Test
    public void testFindTrade() {
        ArrayList<String> i3 = new ArrayList<>();
        i3.add("i10");
        Trade t3 = new Trade("u10", "u11", i3);
        t3.setTradeId("t3");
        tm.addTrade(t3.getTradeId(), t3);
        assertEquals(tm.findTrade("t3"), t3);
    }

    @Test
    public void testCreateNewTrade() {
        ArrayList<String> i3 = new ArrayList<>();
        i3.add("i10");
        String tradeId = tm.createNewTrade("u3", "u4", i3);
        assertEquals(tm.getTradeIdToTrade().size(), 4);
        assert(tm.getTradeIdToTrade().containsKey(tradeId));
        assert(tm.getTradeIdToTrade().containsValue(tm.findTrade(tradeId)));
    }

    @Test
    public void testCreateReverseTrade() {
        String tradeId = tm.createReverseTrade("t1");
        Trade trade = tm.findTrade(tradeId);
        assertEquals(trade.getRequester(), "u2");
        assertEquals(trade.getReceiver(), "u1");
        assertEquals(trade.getDuration(), -1);
        assertEquals(trade.getStatus(), Trade.TradeStatus.ACCEPTED);
        assertEquals(trade.getTradeId(), "@returnt1");
        assert(tm.getTradeIdToTrade().containsKey("@returnt1"));
        assert(tm.getTradeIdToTrade().containsValue(trade));
    }

    @Test
    public void testRequestTrade() {
        ArrayList<String> i3 = new ArrayList<>();
        i3.add("i10");
        tm.requestTrade("u3", "u4", i3, 15);
        assertEquals(tm.getTradeIdToTrade().size(), 4);

        tm.getTradeIdToTrade().remove("t1", t1);
        tm.getTradeIdToTrade().remove("t2", t2);
        tm.getTradeIdToTrade().remove("t11", t11);

        HashMap<String, Trade> hm1 = tm.getTradeIdToTrade();
        ArrayList<String> ar1 = new ArrayList<>();
        ArrayList<Trade> ar2 = new ArrayList<>();

        for (HashMap.Entry<String, Trade> entry: hm1.entrySet()) {
            ar1.add(entry.getKey());
            ar2.add(entry.getValue());
        }

        String tradeId1 = ar1.get(0);
        Trade trade1 = ar2.get(0);

        assertEquals(tm.findTrade(tradeId1), trade1);
        assertEquals(trade1.getRequester(),"u3");
        assertEquals(trade1.getReceiver(), "u4");
        assertEquals(trade1.getItemIds(), i3);
        assertEquals(trade1.getDuration(), 15);
    }

    @Test
    public void testRejectTrade() {
        tm.rejectTrade("t2");
        assertEquals(t2.getStatus(), Trade.TradeStatus.REJECTED);
    }

    @Test
    public void testAcceptTrade() {
        tm.acceptTrade("t2");
        assertEquals(t2.getStatus(), Trade.TradeStatus.ACCEPTED);
    }

    @Test
    public void testMeetingConfirmed() {
        tm.meetingConfirmed("t2");
        assertEquals(t2.getStatus(), Trade.TradeStatus.CONFIRMED);
    }

    @Test
    public void testRequesterConfirmTrade() {
        t1.setStatus(Trade.TradeStatus.CONFIRMED);
        tm.confirmTrade("t1", "u1");
        assertEquals(t1.getStatus(), Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.confirmTrade("t1", "u2");
        assertEquals(t1.getStatus(), Trade.TradeStatus.COMPLETED);
    }

    @Test
    public void testReceiverConfirmTrade() {
        t2.setStatus(Trade.TradeStatus.CONFIRMED);
        tm.confirmTrade("t2", "u4");
        assertEquals(t2.getStatus(), Trade.TradeStatus.RECEIVERCONFIRMED);
        tm.confirmTrade("t2", "u3");
        assertEquals(t2.getStatus(), Trade.TradeStatus.COMPLETED);
    }

    @Test
    public void testCompleteTrade() {
        tm.completeTrade("t2");
        assertEquals(t2.getStatus(), Trade.TradeStatus.COMPLETED);
    }

    @Test
    public void testCancelTrade() {
        tm.cancelTrade("t2");
        assertEquals(t2.getStatus(), Trade.TradeStatus.CANCELLED);
    }

    @Test
    public void testCheckBorrowDiffNoTrade() {
        assert(tm.checkBorrowDiff("u10"));
    }

    @Test
    public void testCheckBorrowDiffLess() {
        tm.completeTrade("t1");
        tm.completeTrade("t2");

        assert(tm.checkBorrowDiff("u1"));
        assert(tm.checkBorrowDiff("u2"));
    }

    @Test
    public void testCheckBorrowDiffMore() {
        tm.completeTrade("t1");
        tm.completeTrade("t2");
        tm.completeTrade("t11");

        assert(!tm.checkBorrowDiff("u1"));
        assert(tm.checkBorrowDiff("u2"));
    }

    @Test
    public void testTradesInOneWeekBasedOnDates() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u2", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(31));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);

        List<String> testList = tm.tradesInOneWeek("u1");
        assert(testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
    }

    @Test
    public void testTradesInOneWeekBasedOnRequesterAndReceiver() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u2", "u1", ef);
        String tGh = tm.createNewTrade("u2", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(3));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.ACCEPTED);

        List<String> testList = tm.tradesInOneWeek("u1");
        assert(testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
    }

    @Test
    public void testTradesInOneWeekBasedOnStatus() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u1", "u2", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(3));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(40);
        tm.findTrade(tCd).setDuration(31);
        tm.findTrade(tEf).setDuration(-1);
        tm.findTrade(tGh).setDuration(-1);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.REJECTED);

        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        List<String> testList = tm.tradesInOneWeek("u1");
        assert(testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntAb));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testCheckTradeLimit() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u2", "u1", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);

        String returntCd = tm.createReverseTrade(tCd);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);
        tm.setWeeklyTradeLimit(3);
        assert(!tm.checkTradeLimit("u1"));
        tm.setWeeklyTradeLimit(4);
        assert(tm.checkTradeLimit("u1"));
    }

    @Test
    public void testCheckIncompleteTrade() {
        assert(tm.checkIncompleteTrade("u1"));
        t1.setStatus(Trade.TradeStatus.CANCELLED);
        assert(!tm.checkIncompleteTrade("u1"));
    }

    @Test
    public void testGetAllStatusByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u1", "u2", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u2", "u1", ef);
        String tGh = tm.createNewTrade("u1", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);

        ArrayList<String> testReq = tm.getAllStatusTradeByUserId("u1", "REQUESTED");
        ArrayList<String> testComp = tm.getAllStatusTradeByUserId("u1", "COMPLETED");
        ArrayList<String> testAcc = tm.getAllStatusTradeByUserId("u1", "ACCEPTED");
        ArrayList<String> testCan = tm.getAllStatusTradeByUserId("u1", "CANCELLED");
        ArrayList<String> testReqCon = tm.getAllStatusTradeByUserId("u1", "REQUESTERCONFIRMED");

        assert(testReq.contains("t1"));
        assert(testComp.contains("t11"));
        assert(testComp.contains(tAb));
        assert(testAcc.contains(tCd));
        assert(testAcc.contains(tEf));
        assert(testAcc.contains(tGh));
        assert(testCan.isEmpty());
        assert(testReqCon.contains(returntCd));
    }

    @Test
    public void testGetAllCompletedBorrowTradeByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> testList = tm.getAllCompletedBorrowTradeByUserId("u1");

        assert(!testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntAb));
    }

    @Test
    public void testGetAllCompletedLendTradeByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> testList = tm.getAllCompletedLendTradeByUserId("u1");

        assert(!testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllBorrowTradeIdByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u4", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.CANCELLED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CONFIRMED);

        ArrayList<String> testList = tm.getAllBorrowTradeIdByUserId("u1");

        assert(!testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllLendTradeIdByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u5", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.CANCELLED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.CANCELLED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CONFIRMED);

        ArrayList<String> testList = tm.getAllLendTradeIdByUserId("u1");

        assert(!testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(!testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllTradeIdByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u5", "u3", ef);
        String tGh = tm.createNewTrade("u4", "u2", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.ACCEPTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CONFIRMED);

        ArrayList<String> testList = tm.getAllTradeIdByUserId("u1");

        assert(testList.contains("t1"));
        assert(testList.contains("t11"));
        assert(testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(!testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(testList.contains(returntCd));
    }

    @Test
    public void testGetAllTwoTradeIdByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u2", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u2", cd);
        String tEf = tm.createNewTrade("u3", "u1", ef);
        String tGh = tm.createNewTrade("u4", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CANCELLED);

        ArrayList<String> testList = tm.getAllTwoWayTradeIdByUserId("u1");

        assert(testList.contains("t1"));
        assert(!testList.contains("t11"));
        assert(!testList.contains(tAb));
        assert(testList.contains(tCd));
        assert(testList.contains(tEf));
        assert(!testList.contains(tGh));
        assert(!testList.contains(returntCd));
    }

    @Test
    public void testGetAllOneWayTradeId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u4", "u6", ab);
        String tCd = tm.createNewTrade("u10", "u21", cd);
        String tEf = tm.createNewTrade("u18", "u33", ef);
        String tGh = tm.createNewTrade("u44", "u15", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CANCELLED);

        HashMap<String, Trade> testHM = tm.getAllOneWayTradeId();

        assert(!testHM.containsKey("t1"));
        assert(testHM.containsKey("t11"));
        assert(!testHM.containsKey(tAb));
        assert(testHM.containsKey(tCd));
        assert(!testHM.containsKey(tEf));
        assert(testHM.containsKey(tGh));
        assert(testHM.containsKey(returntCd));
    }

    @Test
    public void testGetAllTwoWayTradeId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u23", "u14", ab);
        String tCd = tm.createNewTrade("u19", "u26", cd);
        String tEf = tm.createNewTrade("u13", "u33", ef);
        String tGh = tm.createNewTrade("u46", "u11", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REJECTED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.CONFIRMED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.RECEIVERCONFIRMED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.CANCELLED);

        HashMap<String, Trade> testHM = tm.getAllTwoWayTradeId();

        assert(testHM.containsKey("t1"));
        assert(!testHM.containsKey("t11"));
        assert(testHM.containsKey(tAb));
        assert(testHM.containsKey(tCd));
        assert(!testHM.containsKey(tEf));
        assert(!testHM.containsKey(tGh));
        assert(testHM.containsKey(returntCd));
    }

    @Test
    public void testGetRecentItemsBorrowedByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u3", "u1", ab);
        String tCd = tm.createNewTrade("u1", "u4", cd);
        String tEf = tm.createNewTrade("u1", "u33", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(5));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(4));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> test1 = tm.getRecentItemsBorrowedByUserId("u1", 0);
        ArrayList<String> test2 = tm.getRecentItemsBorrowedByUserId("u1", 2);
        ArrayList<String> test3 = tm.getRecentItemsBorrowedByUserId("u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "g");
        assertEquals(test2.get(1), "c");
        assertEquals(test3.get(2), "e");
    }

    @Test
    public void testGetRecentItemsLendByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g"));
        String tAb = tm.createNewTrade("u1", "u3", ab);
        String tCd = tm.createNewTrade("u4", "u1", cd);
        String tEf = tm.createNewTrade("u5", "u1", ef);
        String tGh = tm.createNewTrade("u6", "u1", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> test1 = tm.getRecentItemsLendByUserId("u1", 0);
        ArrayList<String> test2 = tm.getRecentItemsLendByUserId("u1", 2);
        ArrayList<String> test3 = tm.getRecentItemsLendByUserId("u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "e");
        assertEquals(test2.get(1), "g");
        assertEquals(test3.get(2), "c");
    }

    @Test
    public void testGetRecentItemsInTwoWayByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c", "d"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        String tAb = tm.createNewTrade("u4", "u3", ab);
        String tCd = tm.createNewTrade("u4", "u1", cd);
        String tEf = tm.createNewTrade("u1", "u2", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> test1 = tm.getRecentItemsInTwoWayByUserId("u1", 0);
        ArrayList<String> test2 = tm.getRecentItemsInTwoWayByUserId("u1", 2);
        ArrayList<String> test3 = tm.getRecentItemsInTwoWayByUserId("u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 4);
        assertEquals(test3.size(), 6);
        assertEquals(test2.get(0), "e");
        assertEquals(test2.get(1), "f");
        assertEquals(test2.get(2), "g");
        assertEquals(test2.get(3), "h");
        assertEquals(test3.get(4), "c");
        assertEquals(test3.get(5), "d");
    }

    @Test
    public void testGetTopTradersByUserId() {
        ArrayList<String> ab = new ArrayList<>(Arrays.asList("a", "b"));
        ArrayList<String> cd = new ArrayList<>(Arrays.asList("c"));
        ArrayList<String> ef = new ArrayList<>(Arrays.asList("e", "f"));
        ArrayList<String> gh = new ArrayList<>(Arrays.asList("g", "h"));
        ArrayList<String> ij = new ArrayList<>(Arrays.asList("i", "j"));
        ArrayList<String> kl = new ArrayList<>(Arrays.asList("k", "l"));
        String tAb = tm.createNewTrade("u1", "u4", ab);
        String tCd = tm.createNewTrade("u2", "u1", cd);
        String tEf = tm.createNewTrade("u1", "u3", ef);
        String tGh = tm.createNewTrade("u1", "u3", gh);
        String tIj = tm.createNewTrade("u1", "u3", ij);
        String tKl = tm.createNewTrade("u4", "u3", kl);
        tm.findTrade(tAb).setDateCreated(LocalDate.now().minusDays(8));
        tm.findTrade(tCd).setDateCreated(LocalDate.now().minusDays(53));
        tm.findTrade(tEf).setDateCreated(LocalDate.now().minusDays(6));
        tm.findTrade(tGh).setDateCreated(LocalDate.now().minusDays(40));
        tm.findTrade(tIj).setDateCreated(LocalDate.now().minusDays(600));
        tm.findTrade(tKl).setDateCreated(LocalDate.now());
        tm.findTrade(tAb).setDuration(-1);
        tm.findTrade(tCd).setDuration(-1);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        tm.findTrade(tEf).setDuration(3);
        tm.findTrade(tGh).setDuration(4);
        String returntAb = tm.createReverseTrade(tAb);
        String returntCd = tm.createReverseTrade(tCd);

        tm.findTrade("t1").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade("t11").setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tCd).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tEf).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tGh).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(tIj).setStatus(Trade.TradeStatus.REQUESTED);
        tm.findTrade(tKl).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntAb).setStatus(Trade.TradeStatus.COMPLETED);
        tm.findTrade(returntCd).setStatus(Trade.TradeStatus.COMPLETED);

        ArrayList<String> test1 = tm.getTopTradersByUserId("u1", 0);
        ArrayList<String> test2 = tm.getTopTradersByUserId("u1", 2);
        ArrayList<String> test3 = tm.getTopTradersByUserId("u1", 10);

        assert(test1.isEmpty());
        assertEquals(test2.size(), 2);
        assertEquals(test3.size(), 3);
        assertEquals(test2.get(0), "u2");
        assertEquals(test2.get(1), "u3");
        assertEquals(test3.get(2), "u4");
    }

    @Test
    public void testGetTradeStatusByUserId() {
        t1.setStatus(Trade.TradeStatus.REQUESTERCONFIRMED);
        assertEquals(tm.getTradeStatusByTradeId("t1"), Trade.TradeStatus.REQUESTERCONFIRMED);
    }

    @Test
    public void testGetReceiverByTradeId() {
        assertEquals(tm.getReceiverByTradeId("t1"), "u2");
    }

    @Test
    public void testGetRequesterByTradeId() {
        assertEquals(tm.getRequesterByTradeId("t1"), "u1");
    }

    @Test
    public void testGetItemIdsByTradeId() {
        assertEquals(tm.getItemIdsByTradeId("t1"), t1.getItemIds());
    }

    @Test
    public void testGetDurationByTradeId() {
        assertEquals(tm.getDurationByTradeId("t1"), t1.getDuration());
    }

    @Test
    public void testSetMeetingIdByTradeId() {
        tm.setMeetingIdByTradeId("t1", "123");
        assertEquals(tm.getMeetingIdByTradeId("t1"), "123");
    }

    @Test
    public void testGetMeetingIdByTradeId() {
        assertEquals(tm.getMeetingIdByTradeId("t1"), "No Meeting");
    }
}
