package LinguaLoopRequirements {
    doc /* Требования к веб-приложению LinguaLoop (трекер изучения языков). Версия 0.1. */

    import ScalarValues::*;

    // ---------- Участники и система ----------
    part def Learner;
    part def MailServer;

    part def LinguaLoop {
        part cardModule;
        part schedulerModule;
        part statsModule;
        part notificationModule;
        part securityModule;
    }

    // ---------- Базовые определения требований ----------
    requirement def FunctionalRequirement {
        attribute priority : String;   // MoSCoW: Must | Should | Could
        subject system : LinguaLoop;
    }
    requirement def QualityRequirement {
        attribute metric : String;
        subject system : LinguaLoop;
    }

    // ---------- Функциональные требования ----------
    requirement linguaLoopSpec {
        doc /* Корневое требование: планирование повторения слов, статистика, серии занятий, создание карточек. */

        requirement accessAndSettings {
            requirement <'REQ-FUNC-015'> func015 : FunctionalRequirement {
                doc /* Настройки обучения. */
                attribute :>> priority = "Should";
            }
        }

        requirement coursesAndCards {
            requirement <'REQ-FUNC-002'> func002 : FunctionalRequirement {
                doc /* Курсы изучения. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-003'> func003 : FunctionalRequirement {
                doc /* Колоды. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-004'> func004 : FunctionalRequirement {
                doc /* Карточки вручную. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-005'> func005 : FunctionalRequirement {
                doc /* Карточки из текста. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-006'> func006 : FunctionalRequirement {
                doc /* Импорт и экспорт CSV. */
                attribute :>> priority = "Should";
            }
            requirement <'REQ-FUNC-013'> func013 : FunctionalRequirement {
                doc /* Поиск и фильтрация карточек. */
                attribute :>> priority = "Should";
            }
        }

        requirement reviewing {
            requirement <'REQ-FUNC-007'> func007 : FunctionalRequirement {
                doc /* Планирование повторений SM-2. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-008'> func008 : FunctionalRequirement {
                doc /* Сеанс повторения. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-016'> func016 : FunctionalRequirement {
                doc /* Тренировка трудных карточек. */
                attribute :>> priority = "Could";
            }
        }

        requirement goalsAndStats {
            requirement <'REQ-FUNC-009'> func009 : FunctionalRequirement {
                doc /* Дневная цель и лимит новых карточек. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-010'> func010 : FunctionalRequirement {
                doc /* Серии занятий. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-011'> func011 : FunctionalRequirement {
                doc /* Статистика. */
                attribute :>> priority = "Must";
            }
            requirement <'REQ-FUNC-012'> func012 : FunctionalRequirement {
                doc /* График активности и прогноз нагрузки. */
                attribute :>> priority = "Should";
            }
            requirement <'REQ-FUNC-017'> func017 : FunctionalRequirement {
                doc /* Заморозка серии. */
                attribute :>> priority = "Could";
            }
            requirement <'REQ-FUNC-018'> func018 : FunctionalRequirement {
                doc /* Экран «Сегодня». */
                attribute :>> priority = "Should";
            }
        }

        requirement notifications {
            requirement <'REQ-FUNC-014'> func014 : FunctionalRequirement {
                doc /* Напоминание по e-mail. */
                attribute :>> priority = "Could";
            }
        }
    }

    // ---------- Требования к качеству (выборка) ----------
    requirement <'REQ-PERF-002'> cardLatency : QualityRequirement {
        doc /* Выдача следующей карточки и запись оценки не более 300 мс (95 % запросов). */
        attribute :>> metric = "p95 <= 300 ms";
    }
    requirement <'REQ-REL-002'> scheduleIntegrity : QualityRequirement {
        doc /* Оценка, журнал и расписание сохраняются одной транзакцией, повтор оценки идемпотентен. */
        attribute :>> metric = "1 запись на оценку";
    }
    requirement <'REQ-MAINT-001'> schedulerCoverage : QualityRequirement {
        doc /* Покрытие модульными тестами планировщика SM-2 не менее 90 %. */
        attribute :>> metric = "coverage >= 90 %";
    }

    // ---------- Распределение требований по модулям ----------
    part linguaLoop : LinguaLoop;

    satisfy func002 by linguaLoop.cardModule;
    satisfy func004 by linguaLoop.cardModule;
    satisfy func005 by linguaLoop.cardModule;
    satisfy func007 by linguaLoop.schedulerModule;
    satisfy func008 by linguaLoop.schedulerModule;
    satisfy func010 by linguaLoop.statsModule;
    satisfy func011 by linguaLoop.statsModule;
    satisfy func014 by linguaLoop.notificationModule;
    satisfy func001 by linguaLoop.securityModule;
    satisfy cardLatency by linguaLoop.schedulerModule;
    satisfy scheduleIntegrity by linguaLoop.schedulerModule;
    satisfy schedulerCoverage by linguaLoop.schedulerModule;

    // ---------- Варианты использования ----------
    use case def ManageCards {
        subject system : LinguaLoop;
        actor user : Learner;
        objective { doc /* Создавать и вести карточки: вручную, из текста, из CSV. */ }
    }
    use case def ReviewCards {
        subject system : LinguaLoop;
        actor user : Learner;
        objective { doc /* Пройти сеанс повторения и оценить ответы. */ }
    }
    use case def TrackProgress {
        subject system : LinguaLoop;
        actor user : Learner;
        actor mail : MailServer;
        objective { doc /* Следить за целью, серией и статистикой, получать напоминания. */ }
    }
}
