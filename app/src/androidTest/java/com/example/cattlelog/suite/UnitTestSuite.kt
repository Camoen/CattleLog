package com.example.cattlelog.suite

import com.example.cattlelog.CattleDaoInstrumentedTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(CattleDaoInstrumentedTest::class) /* Pass in other instrumented unit tests to the parameter list */
class UnitTestSuite