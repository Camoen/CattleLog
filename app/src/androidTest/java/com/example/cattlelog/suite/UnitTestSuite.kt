package com.example.cattlelog.suite

import com.example.cattlelog.CattleDaoInstrumentedTest
import com.example.cattlelog.HealthDaoInstrumentedTest
import com.example.cattlelog.TreatmentDaoInstrumentedTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(CattleDaoInstrumentedTest::class,
                    HealthDaoInstrumentedTest::class,
                    TreatmentDaoInstrumentedTest::class)
class UnitTestSuite