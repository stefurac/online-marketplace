package usecases;

import java.util.*;
import entities.Trade;

/**
 * TradeComparator is to compare trades
 */
public class TradeComparator implements Comparator<Trade> {

    /**
     * Compares which trade is more recent
     * @param t1 trade1
     * @param t2 trade2
     * @return integer indicating whether the trade is created earlier or after
     */
    @Override
    public int compare(Trade t1, Trade t2) {
        return t1.getDateCreated().compareTo(t2.getDateCreated());
    }
}
