import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classes from './DictionariesTable.module.scss';

import { Table, Spinner } from '../../UI';
import TableControl from './TableControl/TableControl';
import { useBreadcrumbs, useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

const DictionariesTable = () => {    
    useBreadcrumbs('Dictionaries');
    useActionOnMount(actions.fetchAllDictionaries());
    const dispatch = useDispatch();
    const loading = useSelector(selectors.loadingDictionariesSelector);
    const data = useSelector(selectors.dictionariesTableDataSelector);

    const COLUMNS_DEF = [
        { name: 'Name', label: 'name' },
        { name: 'Language', label: 'language' },
    ];

    const rowOnClickCb = (rowData, i) => {
        dispatch(actions.selectDictionary(i));
    }

    const table = loading ? <Spinner /> : (
        <Table 
            columnsDef={COLUMNS_DEF} 
            data={data} 
            rowOnClickCb={rowOnClickCb}
            selectable
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
