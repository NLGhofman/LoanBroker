/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common;

import java.util.Vector;

/**
 *
 * @author Maja Pesic
 */
public abstract class RequestReplyRow<REQUEST, REPLY> extends Vector {

        private REQUEST request;
        private REPLY reply;
        private int nr;

        public RequestReplyRow(REQUEST request, int index) {
            super();
            this.request = request;
            this.reply = null;
            fillRequestCells(request);
            nr = index;
        }


      public RequestReplyRow(int index) {
            super();
            this.request = null;
            this.reply = null;
            fillRequestCells(null);
            nr = index;
        }

        protected abstract void fillRequestCells(REQUEST request);

        public void setReply(REPLY reply) {
            this.reply = reply;
            fillReplyCells(reply);
        }

        protected abstract void fillReplyCells(REPLY reply);

        public REPLY getReply() {
            return reply;
        }

        public REQUEST getRequest() {
            return request;
        }

        protected int getIndex(){
            return nr;
        }
    }
