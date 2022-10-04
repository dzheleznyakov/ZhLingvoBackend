import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './QuizzesTable.module.scss';

import * as selectors from '../../../store/selectors';
import { Spinner, Table } from '../../UI';
import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';
import { useActionOnMount, useBreadcrumbs } from '../../../hooks';
import * as actions from '../../../store/actions';
import TableControl from './TableControl/TableControl';

const BREADCRUMB = { type: BREADCRUMBS_TYPES.TEXT, text: 'Tutor' };

const QuizzesTable = () => {
    useBreadcrumbs(BREADCRUMB);
    useActionOnMount(actions.fetchAllQuizzes());
    const loading = useSelector(selectors.loadingQuizzesSelector);
    const data = useSelector(selectors.quizzesTableDataSelector);
    const selectedQuizIndex = useSelector(selectors.selectedQuizIndexSelector);
    const dispatch = useDispatch();

    const COLUMNS_DEF = [
        { name: 'Name', label: 'name' },
        { name: 'Target Language', label: 'language' },
    ]

    const rowOnClickCb = (_, i) => {
        dispatch(actions.selectQuiz(i));
    };

    const rowOnDbClickCb = (rowData) => {
        dispatch(actions.navigateTo(`/tutor/quiz/${rowData[0].id}`));
    };
    
    const table = loading ? <Spinner /> : (
        <Table
            columnsDef={COLUMNS_DEF}
            data={data}
            rowOnClickCb={rowOnClickCb}
            rowOnDbClickCb={rowOnDbClickCb}
            selectable
            selectedRowIndex={selectedQuizIndex}
        />
    );
    const control = loading ? null : <TableControl />;

    return (
        <div className={classes.QuizzesTableWrapper}>
            {table}
            {control}
        </div>
    )
};


export default QuizzesTable;
