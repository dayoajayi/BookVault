@ApplicationModule(
        allowedDependencies = {
                "catalog",
                "time",
                "borrowingevents",
                "accountingevents"
        }
)
package com.example.BookVault.borrowing;

import org.springframework.modulith.ApplicationModule;
