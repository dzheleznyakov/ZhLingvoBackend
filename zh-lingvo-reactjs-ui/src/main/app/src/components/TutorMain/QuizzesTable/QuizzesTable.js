import React from 'react';
import { useSelector } from 'react-redux';

import classes from './QuizzesTable.module.scss';

import * as selectors from '../../../store/selectors';
import { Spinner, Table } from '../../UI';
import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';
import { useActionOnMount, useBreadcrumbs } from '../../../hooks';
import * as actions from '../../../store/actions';

const BREADCRUMB = { type: BREADCRUMBS_TYPES.TEXT, text: 'Tutor' };

const QuizzesTable = () => {
    useBreadcrumbs(BREADCRUMB);
    useActionOnMount(actions.fetchAllQuizzes());
    const loading = useSelector(selectors.loadingQuizzesSelector);
    const data = useSelector(selectors.quizzesTableDataSelector);

    const COLUMNS_DEF = [
        { name: 'Name', label: 'name' },
        { name: 'Target Language', label: 'language' },
    ]
    
    const table = loading ? <Spinner /> : (
        <Table
            columnsDef={COLUMNS_DEF}
            data={data}
            selectable
        />
    );

    return (
        <div className={classes.QuizzesTableWrapper}>
            {table}
        </div>
    )
};


export default QuizzesTable;
