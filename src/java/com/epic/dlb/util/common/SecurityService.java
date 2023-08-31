/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.util.common;

import com.epic.dlb.model.DlbWbEmployee;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.springframework.stereotype.Component;

/**
 *
 * @author kasun_n
 */
@Component("securityService")
public class SecurityService {

    public boolean verifyText(String publicKeyString, byte[] orgParam, byte[] signedParam) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(publicKeyBytes); ObjectInputStream ois = new ObjectInputStream(bais)) {

            PublicKey publicKey = (PublicKey) ois.readObject();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(orgParam);

            return signature.verify(signedParam);

        } catch (Exception ex) {
            Logger.getLogger(SecurityService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean verifyText(HttpSession session, byte[] orgParam, byte[] signedParam) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        DlbWbEmployee employee = (DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE);
        String publicKeyString = employee.getTokenPublicKey();
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(publicKeyBytes); ObjectInputStream ois = new ObjectInputStream(bais)) {

            PublicKey publicKey = (PublicKey) ois.readObject();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(orgParam);

            return signature.verify(signedParam);

        }

    }
}
