import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './TableRow.module.scss';

import * as types from '../types';
import { selectedDictionaryIndexSelector } from '../../../../store/selectors';
import { useSelector } from 'react-redux';

const TableRow = ({ rowData, rowIndex, rowOnClickCb, rowOnDbClickCb, children, selectable }) => {
    const [cssClasses, setCssClasses] = useState([]);
    const selectedIndex = useSelector(selectedDictionaryIndexSelector);
    const ref = useRef();
    const trRef = selectable ? ref : null;

    useEffect(() => {
        if (selectable && selectedIndex === rowIndex)
            setCssClasses(cssClasses.concat([classes.Focused]))
        else {
            const updatedCssClasses = [].concat(cssClasses);
            const index = updatedCssClasses.indexOf(classes.Focused);
            updatedCssClasses.splice(index, 1);
            setCssClasses(updatedCssClasses);
        }
    }, [selectedIndex, trRef]);

    const onClick = () => {
        rowOnClickCb && rowOnClickCb(rowData, rowIndex);
    };

    const onDoubleClick = () => {
        rowOnDbClickCb && rowOnDbClickCb(rowData, rowIndex);
    };

    return (
        <tr 
            className={cssClasses.join(' ')}
            onClick={onClick}
            onDoubleClick={onDoubleClick}
            ref={trRef}
        >
            {children}
        </tr>
    );
};

TableRow.propTypes = {
    rowIndex: PropTypes.number.isRequired,
    rowData: PropTypes.arrayOf(types.dataCellDef),
    rowOnClickCb: PropTypes.func,
    rowOnDbClickCb: PropTypes.func,
    children: PropTypes.node,
    selectable: PropTypes.bool,
};

export default TableRow;
