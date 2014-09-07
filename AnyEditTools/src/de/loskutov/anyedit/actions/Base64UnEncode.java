/*******************************************************************************
 * Copyright (c) 2009 Andrey Loskutov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor:  Stefan Seidel - initial API and implementation
 * Contributor:  Andrey Loskutov - platform independent refactoring and integration
 *******************************************************************************/
package de.loskutov.anyedit.actions;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import de.loskutov.anyedit.AnyEditToolsPlugin;
import de.loskutov.anyedit.IAnyEditConstants;
import de.loskutov.anyedit.util.TextReplaceResultSet;

public class Base64UnEncode extends AbstractReplaceAction {

    private static final int KEY_DECODE = 0;

    private static final int KEY_ENCODE = 1;

    private boolean splitLines;

    @Override
    protected int getActionKey(String actionID) {
        return actionID.startsWith(AbstractTextAction.ACTION_ID_ENCODE) ? KEY_ENCODE
                : KEY_DECODE;
    }

    @Override
    protected void doTextOperation(IDocument doc, String actionID,
            TextReplaceResultSet resultSet) throws BadLocationException {
        splitLines = splitLinesEnabled();
        super.doTextOperation(doc, actionID, resultSet);
    }

    private boolean splitLinesEnabled() {
        return AnyEditToolsPlugin.getDefault().getPreferenceStore().getBoolean(
                IAnyEditConstants.BASE64_SPLIT_LINE);
    }

    @Override
    protected String performReplace(String line, int actionKey) {
        String charset = getEditor().computeEncoding();
        if (actionKey == KEY_DECODE) {
            // not portable code
            // try {
            // return new String(new BASE64Decoder().decodeBuffer(line));
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            return textUtil.base64decode(line, charset);
        }
        // not portable code
        // return trimBase64Encoded(new BASE64Encoder().encode(line.getBytes()));
        if(splitLines) {
            return textUtil.base64trim(textUtil.base64encode(line, charset), getEditor()
                    .getDocument().getLegalLineDelimiters()[0]);
        }
        return textUtil.base64encode(line, charset);
    }

}
