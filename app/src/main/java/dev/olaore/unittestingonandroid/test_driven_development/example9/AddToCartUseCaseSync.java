package dev.olaore.unittestingonandroid.test_driven_development.example9;


import dev.olaore.unittestingonandroid.test_driven_development.example9.networking.AddToCartHttpEndpointSync;
import dev.olaore.unittestingonandroid.test_driven_development.example9.networking.CartItemScheme;
import dev.olaore.unittestingonandroid.test_driven_development.example9.networking.NetworkErrorException;

public class AddToCartUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR;
    }

    private final AddToCartHttpEndpointSync mAddToCartHttpEndpointSync;

    public AddToCartUseCaseSync(AddToCartHttpEndpointSync addToCartHttpEndpointSync) {
        mAddToCartHttpEndpointSync = addToCartHttpEndpointSync;
    }

    public UseCaseResult addToCartSync(String offerId, int amount) {
        AddToCartHttpEndpointSync.EndpointResult result;

        try {
            result = mAddToCartHttpEndpointSync.addToCartSync(new CartItemScheme(offerId, amount));
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        switch (result) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case AUTH_ERROR:
            case GENERAL_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("invalid endpoint result: " + result);
        }
    }
}
