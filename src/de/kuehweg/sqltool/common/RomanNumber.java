/*
 * Copyright (c) 2013, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.sqltool.common;

/**
 *
 * @author Michael Kühweg
 */
public class RomanNumber {

    private final long value;

    public RomanNumber(final long value) {
        this.value = value;
    }

    private enum Digit {

        I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400),
        D(500), CM(900), M(1000);
        int value;

        private Digit(int value) {
            this.value = value;
        }

        public static Digit fit(final long number) {
            for (int i = values().length - 1; i >= 0; i--) {
                if (values()[i].value <= number) {
                    return values()[i];
                }
            }
            throw new IllegalArgumentException("Cannot fit " + number
                    + " into RomanNumber");
        }
    };

    public String toRomanNumber() {
        StringBuilder builder = new StringBuilder();
        long remainder = value;
        while (remainder > 0) {
            final Digit romanDigit = Digit.fit(remainder);
            builder.append(romanDigit.name());
            remainder -= romanDigit.value;
        }
        return builder.toString();
    }
}
