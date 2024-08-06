package ru.gb.timesheet.example;

//вычисляет стоимость товара с учетом НДС
public class TaxCalculator {

    private final TaxResolver taxResolver;

    public TaxCalculator(TaxResolver taxResolver) {
        this.taxResolver = taxResolver;
    }

    public double getPriceWithTax(double price) {
        double currentTax = taxResolver.getCurrentTax();
        return price + price * currentTax;
    }
}

