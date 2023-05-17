package com.juliuskrah.camel.consumer.common.api;

import com.juliuskrah.camel.consumer.common.exceptions.CamelProcessingException;
import com.juliuskrah.camel.consumer.common.wrappers.MerchantPayment;
import com.juliuskrah.camel.consumer.common.wrappers.Response;

/**
 * This is the public API consumers of this starter must invoke
 * 
 * @author Julius Krah
 */
@FunctionalInterface
public interface MerchantIntegration {

     <T extends Response> T invoke(MerchantPayment merchantPayment) throws CamelProcessingException;
}
