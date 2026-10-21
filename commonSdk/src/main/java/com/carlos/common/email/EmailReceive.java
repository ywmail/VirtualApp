/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  javax.mail.MessagingException
 */
package com.carlos.common.email;

import android.text.TextUtils;
import com.carlos.common.email.LeaveMessage;
import com.carlos.common.email.MailUtil;
import com.kook.common.utils.HVLog;
import com.kook.librelease.StringFog;
import java.security.GeneralSecurityException;
import javax.mail.MessagingException;

public class EmailReceive {
    public static EmailReceive mEmailReceive = new EmailReceive();

    public static EmailReceive getInstance() {
        return mEmailReceive;
    }

    public void sendCode(LeaveMessage leaveMessage) {
        MailUtil mailUtil = new MailUtil("serven_scorpion@foxmail.com", "mkvjauphcaixcbcc");
        try {
            String receiveAccount = leaveMessage.getReceiveAccount();
            if (TextUtils.isEmpty((CharSequence)receiveAccount)) {
                receiveAccount = "329716228@qq.com";
            }
            mailUtil.send(receiveAccount, leaveMessage.getTitle(), leaveMessage.getContent());
            HVLog.d("邮件发送成功");
        }
        catch (MessagingException e) {
            HVLog.d("邮件错误：" + e.getMessage());
        }
        catch (GeneralSecurityException e) {
            HVLog.d("邮件错误：" + e.getMessage());
        }
    }
}

