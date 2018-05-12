package ru.mifsan.javacore2017.customer;

import org.junit.jupiter.api.Test;
import ru.mifsan.javacore2017.payment.Bill;
import ru.mifsan.javacore2017.payment.PaymentMethod;
import ru.mifsan.javacore2017.product.Product;
import ru.mifsan.javacore2017.product.ProductImpl;
import ru.mifsan.javacore2017.product.ProductType;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerImplTest {

    private static final CustomerType customerType = CustomerType.Child;
    private static final int customerCash = 10;
    private static final int customerCardCash = 100;
    private static final int customerBonusCount = 1000;
    private final Customer customer =
            new CustomerImpl(CustomerType.Child, customerCash, customerCardCash, customerBonusCount);

    @Test
    void defaultIdIsZero() {
        assertEquals(0, customer.getId());
    }

    @Test
    void hasGettersForCashes() {
        assertEquals(customerCash, customer.getCash());
        assertEquals(customerCardCash, customer.getCardCash());
        assertEquals(customerBonusCount, customer.getBonusCount());
    }

    @Test
    void setId() {
        int id = 1;
        customer.setId(id);
        assertEquals(id, customer.getId());
    }

    @Test
    void getType() {
        assertEquals(customerType, customer.getType());
    }

    @Test
    void getBonusCount() {
        assertEquals(customerBonusCount, customer.getBonusCount());
    }

    @Test
    void getBucketIsEmptyByDefault() {
        assertTrue(customer.getBucket().isEmpty());
    }

    @Test
    void inQueueIsFalseByDefault() {
        assertFalse(customer.isInQueue());
    }

    @Test
    void setInQueue() {
        customer.setInQueue(true);
        assertTrue(customer.isInQueue());
        customer.setInQueue(false);
        assertFalse(customer.isInQueue());
    }

    @Test
    void getNameConcatenatesTypeAndId() {
        assertEquals(customerType.toString() + ' ' + customer.getId(), customer.getName());
    }

    @Test
    void pay() {
        Bill bill = new Bill();
        bill.add(new ProductImpl(1, "Продукт", 1000, 1, ProductType.Packed, 0, false));
        Product product = new ProductImpl(2, "Ещё продукт", 1, 1, ProductType.Packed, 0, false);
        bill.add(product);
        assertFalse(customer.pay(bill));
        bill.remove(product);
        assertTrue(customer.pay(bill));
        assertEquals(0, customer.getBonusCount());
        Bill easyBill = new Bill();
        easyBill.add(product);
        assertTrue(customer.pay(easyBill));
    }

    @Test
    void getDesiredPaymentMethod() {
        assertNull(customer.getDesiredPaymentMethod(customerBonusCount + 1));
        assertEquals(PaymentMethod.Bonuses, customer.getDesiredPaymentMethod(customerCardCash + 1));
    }

}