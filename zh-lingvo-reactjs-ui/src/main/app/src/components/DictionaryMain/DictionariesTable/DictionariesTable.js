import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './DictionariesTable.module.scss';

import { Table, Spinner } from '../../UI';
import TableControl from './TableControl/TableControl';
import { useBreadcrumbs, useActionOnMount } from '../../../hooks';
import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

const BREADCRUMB = { type: BREADCRUMBS_TYPES.TEXT, text: 'Dictionaries' };

const DictionariesTable = () => {    
    useBreadcrumbs(BREADCRUMB);
    useActionOnMount(actions.fetchAllDictionaries());
    const dispatch = useDispatch();
    const selectedIndex = useSelector(selectors.selectedDictionaryIndexSelector);
    const loading = useSelector(selectors.loadingDictionariesSelector);
    const data = useSelector(selectors.dictionariesTableDataSelector);

    const COLUMNS_DEF = [
        { name: 'Name', label: 'name' },
        { name: 'Language', label: 'language' },
    ];

    const rowOnClickCb = (_, i) => {
        dispatch(actions.selectDictionary(i));
    };

    const rowOnDbClickCb = (rowData) => {
        dispatch(actions.navigateTo(`/dictionaries/${rowData[0].id}`));
    };

    const table = loading ? <Spinner /> : (
        <Table 
            columnsDef={COLUMNS_DEF} 
            data={data} 
            rowOnClickCb={rowOnClickCb}
            rowOnDbClickCb={rowOnDbClickCb}
            selectable
            selectedRowIndex={selectedIndex}
        />
    );
    const control = loading ? null : <TableControl />

    return (
        <div className={classes.DictionariesTableWrapper}>
            {table}
            {control}
        </div>
    );
};

export default DictionariesTable;
