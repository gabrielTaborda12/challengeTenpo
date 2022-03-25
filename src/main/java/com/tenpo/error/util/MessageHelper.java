package com.tenpo.error.util;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

@Service
public class MessageHelper {
    private MessageSource messageSource;
    private MessageSourceAccessor messageSourceAccessor;
    private Locale contextLocale = LocaleContextHolder.getLocale();

    public MessageHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.messageSourceAccessor = new MessageSourceAccessor(this.messageSource, contextLocale);
    }

    public String resolve(String messageId, String... messageArgs) {
        return this.messageSourceAccessor.getMessage(messageId, messageArgs);
    }

}
