package com.yibo.yiboapp.manager

object ManagerFactory {

    val generalManager by lazy { GeneralManager() }

    val userManager by lazy { UserManager() }

    val bankingManager by lazy { BankingManager() }

    val gameManager by lazy { GameManager() }
}