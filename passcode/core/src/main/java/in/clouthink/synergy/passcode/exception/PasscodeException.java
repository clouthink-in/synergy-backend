package in.clouthink.synergy.passcode.exception;

import in.clouthink.synergy.exception.BizException;

/**
 * @author dz
 */
public class PasscodeException extends BizException {

	public PasscodeException() {
	}

	public PasscodeException(String message) {
		super(message);
	}

	public PasscodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasscodeException(Throwable cause) {
		super(cause);
	}

	public PasscodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
