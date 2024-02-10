import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizRunsTable.module.scss';
import { ControlBox, Greeting, MODAL_TYPES, Table } from '../../UI';
import { useActionOnMount, useConditionalActionOnMount } from '../../../hooks';
import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';

const QuizRunsTable = () => {
    const { id: selectedQuizId } = useSelector(selectors.selectedQuizSelector);
    const { id: quizId } = useSelector(selectors.selectedQuizSelector);
    const quizRuns = useSelector(selectors.allQuizRunsSelector);
    const dispatch = useDispatch();

    useConditionalActionOnMount(actions.fetchAllQuizRuns(quizId), quizId != null, quizId);

    const columnsDef = [
        { name: '', label: 'ts' },
        { name: 'Remains', label: 'remains' },
        { name: 'Actions', label: 'actions' },
    ];

    const data = quizRuns.map(qr => ({
        ts: { value: new Date(qr.ts).toLocaleString() },
        remains: { value: `${qr.records.length} record${qr.records.length !== 1 ? 's' : ''}` },
        actions: { value: (
            <div>
                <ControlBox 
                    panelKeyPrefix={`quizRun-${quizId}-${qr.id}-`}
                    disabled={false}
                    panels={[
                        {
                            modalType: MODAL_TYPES.PLAY,
                            clicked: () => dispatch(actions.navigateTo(`/quiz/${quizId}/run/${qr.id}`)),
                            disabled: qr.records.length === 0,
                        },
                        {
                            modalType: MODAL_TYPES.DELETE,
                            clicked: () => dispatch(actions.deleteQuizRun(quizId, qr.id)),
                        },
                    ]}
                />
            </div>) },
    }));

    return selectedQuizId != null && quizRuns.length > 0 && (
        <div className={classes.QuizRunsTableWrapper}>
            <Greeting subtitle="Quiz runs:" />
            <div className={classes.QuizRunsTable}>
                <Table
                    columnsDef={columnsDef}
                    data={data}
                />
            </div>
        </div>
    );
};

export default QuizRunsTable;
