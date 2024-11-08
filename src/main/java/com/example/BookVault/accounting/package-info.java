@ApplicationModule(
        allowedDependencies = {
                "time",
                "borrowingevents",
                "accountingevents"
        }
)
package com.example.BookVault.accounting;

import org.springframework.modulith.ApplicationModule;
