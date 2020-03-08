package de.clayntech.dentahl4j.domain;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class ErrorMessage
{

    private final String message;

    public ErrorMessage()
    {
        this(null);
    }

    public ErrorMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

}
