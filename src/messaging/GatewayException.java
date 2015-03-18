/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

/**
 * A generic exception for gateways.
 * @author Zef
 */
public class GatewayException extends Exception
{
    /**
     * Create a new GatewayException.
     * @param message The message on why this exception was thrown.
     * @param cause The throwable that caused this exception.
     */
    public GatewayException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
