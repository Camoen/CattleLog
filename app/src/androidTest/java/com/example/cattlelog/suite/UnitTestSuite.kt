package com.example.cattlelog.suite

import com.example.cattlelog.CattleDaoInstrumentedTest
import com.example.cattlelog.HealthDaoInstrumentedTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(CattleDaoInstrumentedTest::class, HealthDaoInstrumentedTest::class)
class UnitTestSuite