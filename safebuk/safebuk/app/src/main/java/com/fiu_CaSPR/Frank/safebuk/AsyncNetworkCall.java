package com.fiu_CaSPR.Frank.safebuk;

/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public interface AsyncNetworkCall<RequestType,ResponseType>
{
    public void execute(RequestType request);
    public void onSuccess(ResponseType response);
    public void onError(Error error);

}
