import React from 'react';
import PropTypes from 'prop-types';

import * as types from '../types';

const TableCell = ({ cellData, colIndex, cellOnClickCb }) => {
    const value = cellData.value;
    return <td onClick={cellOnClickCb && (() => cellOnClickCb(cellData, colIndex))}>{value}</td>;
};

TableCell.propTypes = {
    cellData: types.dataCellDef.isRequired,
    colIndex: PropTypes.number,
    cellOnClickCb: PropTypes.func,
}

export default TableCell;
