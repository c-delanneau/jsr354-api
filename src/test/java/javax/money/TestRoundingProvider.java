/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE.
 * 
 * Specification: JSR-354 Money and Currency API ("Specification")
 * 
 * Copyright (c) 2012-2013, Credit Suisse All rights reserved.
 */
package javax.money;

import javax.money.spi.RoundingProviderSpi;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TestRoundingProvider implements RoundingProviderSpi{

    @Override
    public MonetaryRounding getRounding(RoundingQuery roundingQuery){
        List<MonetaryRounding> result = new ArrayList<>();
        if(roundingQuery.getRoundingName() != null){
            return getCustomRounding(roundingQuery.getRoundingName());
        }
        if(roundingQuery.getCurrencyUnit() != null){
            return getCustomRounding(roundingQuery.getCurrencyUnit().getCurrencyCode());
        }
        return getCustomRounding("test");
    }

    @Override
    public Set<QueryType> getQueryTypes() {
        return QueryType.DEFAULT_SET;
    }

    private MonetaryRounding getCustomRounding(final String customRoundingId){
        return new MonetaryRounding(){

            private final RoundingContext CTX =
                    RoundingContextBuilder.create("TestRoundingProvider", customRoundingId).build();

            @Override
            public RoundingContext getRoundingContext(){
                return CTX;
            }

            @Override
            public MonetaryAmount apply(MonetaryAmount monetaryAmount){
                switch(customRoundingId){
                    case "custom1":
                        return monetaryAmount.multiply(2);
                    case "custom2":
                        return monetaryAmount.multiply(3);
                    default:
                        return monetaryAmount;
                }
            }
        };
    }


    @Override
    public Set<String> getRoundingIds(){
        Set<String> result = new HashSet<>();
        result.add("custom1");
        result.add("custom2");
        return result;
    }

}
