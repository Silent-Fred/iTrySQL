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

        M(1000), CM(900), D(500), CD(400), C(100), XC(90), L(50), XL(40), X(10),
        IX(9), V(5), IV(4), I(1);
        int value;

        private Digit(int value) {
            this.value = value;
        }

        public static Digit fit(final long number) throws IllegalArgumentException {
            for (final Digit digit : values()) {
                if (digit.value <= number) {
                    return digit;
                }
            }
            throw new IllegalArgumentException("Cannot fit " + number
                    + " into RomanNumber");
        }
    };

    @Override
    public String toString() {
        try {
            StringBuilder builder = new StringBuilder();
            long remainder = value;
            Digit romanDigit;
            do {
                romanDigit = Digit.fit(remainder);
                builder.append(romanDigit.name());
                remainder -= romanDigit.value;
            } while (remainder > 0);
            return builder.toString();
        } catch (IllegalArgumentException ex) {
            return "mendum";
        }
    }
}
