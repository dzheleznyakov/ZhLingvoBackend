import React, { useEffect, useRef, useState } from 'react';
import PropTypes from 'prop-types';

import classes from './TableRow.module.scss';

import * as types from '../types';

const TableRow = ({ 
    rowData, 
    rowIndex, 
    rowOnClickCb, 
    rowOnDbClickCb, 
    children, 
    selectable, 
    selected,
}) => {
    const [cssClasses, setCssClasses] = useState([]);
    const ref = useRef();
    const trRef = selectable ? ref : null;

    useEffect(() => {
        if (selectable && selected)
            setCssClasses(cssClasses.concat([classes.Focused]))
        else {
            const updatedCssClasses = [].concat(cssClasses);
            const index = updatedCssClasses.indexOf(classes.Focused);
            updatedCssClasses.splice(index, 1);
            setCssClasses(updatedCssClasses);
        }
    }, [selected, selectable, trRef]); // eslint-disable-line react-hooks/exhaustive-deps

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
    selected: PropTypes.bool,
};

export default TableRow;
