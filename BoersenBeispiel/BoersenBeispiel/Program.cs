using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BoersenBeispiel
{
    class Program
    {
        static void Main(string[] args)
        {
            Customer cust1 = new Customer("Stephanie", "Kaschnitz", "05.07.1992", "Testaddresse 1", "Salzburg", "06641234567");
            Account acc1 = new Account(cust1, 100);

            Customer cust2 = new Customer("Christopher", "Wieland", "10.10.1993", "Testaddresse 2", "Salzburg", "06641278567");
            Account acc2 = new Account(cust2, 100.5);
        }
    }
}
