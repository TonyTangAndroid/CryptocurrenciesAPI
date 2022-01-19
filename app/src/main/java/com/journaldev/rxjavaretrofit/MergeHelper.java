package com.journaldev.rxjavaretrofit;

import com.journaldev.rxjavaretrofit.pojo.CoinMarket;
import com.journaldev.rxjavaretrofit.pojo.CryptoDataModel;
import io.reactivex.Observable;
import java.util.Comparator;
import java.util.List;

public class MergeHelper {

  static List<CoinMarket> mergeAndSort(CryptoDataModel btc, CryptoDataModel eth) {
    Observable<CoinMarket> btcStream = coinMarketStream(btc);
    Observable<CoinMarket> ethStream = coinMarketStream(eth);
    return Observable.merge(btcStream, ethStream)
        .sorted(new Comparator<CoinMarket>() {
          @Override
          public int compare(CoinMarket o1, CoinMarket o2) {
            return Float.compare(o2.market.volume, o1.market.volume);
          }
        }).toList().blockingGet();
  }

  static Observable<CoinMarket> coinMarketStream(CryptoDataModel btc) {
    return Observable.fromIterable(btc.serverCoinModel.markets)
        .map(market -> new CoinMarket(btc.coinName, market));
  }
}
