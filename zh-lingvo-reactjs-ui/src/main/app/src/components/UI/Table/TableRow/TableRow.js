import React, { useRef } from 'react';
import PropTypes from 'prop-types';

import * as types from '../types';

const TableRow = ({ rowData, rowIndex, rowOnClickCb, children, selectable }) => {
    const ref = useRef();
    const trRef = selectable ? ref : null;
    const onClick = () => {
        trRef && trRef.current.focus();
        rowOnClickCb && rowOnClickCb(rowData, rowIndex);
    };
    return (
        <tr 
            tabIndex={rowIndex}
            onClick={onClick}
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
    children: PropTypes.node,
    selectable: PropTypes.bool,
};

export default TableRow;
