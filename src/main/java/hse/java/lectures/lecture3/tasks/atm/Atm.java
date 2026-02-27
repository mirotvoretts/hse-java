package hse.java.lectures.lecture3.tasks.atm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Atm {
    public enum Denomination {
        D50(50),
        D100(100),
        D500(500),
        D1000(1000),
        D5000(5000);

        private final int value;

        Denomination(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }
    }

    private Map<Denomination, Integer> banknotes = new EnumMap<>(Denomination.class);

    private Denomination getDenominationByValue(int value) {
        for (Denomination denomination : Denomination.values()) {
            if (denomination.value() == value) {
                return denomination;
            }
        }
        throw new IllegalArgumentException("Invalid denomination value: " + value);
    }

    public Atm() {
    }

    public void deposit(Map<Integer, Integer> bills) {
        if (bills == null) {
            throw new InvalidDepositException("Bills map cannot be null");
        }

        for (Map.Entry<Integer, Integer> entry : bills.entrySet()) {
            int denominationValue = entry.getKey();
            int count = entry.getValue();

            if (count <= 0) {
                throw new InvalidDepositException("Count must be positive for denomination: " + denominationValue);
            }

            Denomination denomination;
            try {
                denomination = getDenominationByValue(denominationValue);
            } catch (IllegalArgumentException e) {
                throw new InvalidDepositException("Invalid denomination: " + denominationValue);
            }

            banknotes.put(denomination, banknotes.getOrDefault(denomination, 0) + count);
        }
    }

    public Map<Integer, Integer> withdraw(int amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }

        if (amount > getBalance()) {
            throw new InsufficientFundsException("Not enough funds in the ATM");
        }

        Map<Integer, Integer> result = new HashMap<>();
        var banknoteCopy = new EnumMap<>(banknotes);


        for (int i = Denomination.values().length - 1; i >= 0; i--) {
            Denomination denomination = Denomination.values()[i];
            int availableCount = banknotes.getOrDefault(denomination, 0);
            int neededCount = amount / denomination.value();
            int countToDispense = Math.min(availableCount, neededCount);

            if (countToDispense > 0) {
                result.put(denomination.value(), countToDispense);
                amount -= countToDispense * denomination.value();
                banknotes.put(denomination, availableCount - countToDispense);
            }
        }

        if (amount != 0) {
            banknotes = banknoteCopy;
            throw new CannotDispenseException("Not enough funds in the ATM");
        }

        return result;
    }

    public int getBalance() {
        int result = 0;
        for (Map.Entry<Denomination, Integer> entry : banknotes.entrySet()) {
            Denomination denomination = entry.getKey();
            int count = entry.getValue();
            result += count * denomination.value();
        }
        return result;
    }
}