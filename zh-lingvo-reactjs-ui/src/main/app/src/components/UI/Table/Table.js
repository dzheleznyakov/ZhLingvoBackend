import React from 'react';
import PropTypes from 'prop-types';

import classes from './Table.module.scss';

import * as types from './types';
import TableRow from './TableRow/TableRow';
import EmptyTableRow from './TableRow/EmptyTableRow';
import TableCell from './TableCell/TableCell';

const Table = props => {
    const { columnsDef, data, rowOnClickCb, rowOnDbClickCb, selectedRowIndex } = props;

    const headers = columnsDef.map(({ name, label }) => <th key={label}>{name}</th>);
    let body = data.map((d, i) => {
        const rowData = columnsDef.map(def => d[def.label]);
        const cells = columnsDef.map((def, colIndex) => (
            <TableCell 
                key={`${def.label}-${colIndex}`} 
                cellData={rowData[colIndex]} 
                colIndex={colIndex} 
                cellOnClickCb={def.cellOnClickCb} 
            />
        ));
        const rowIsSelected = i === selectedRowIndex;
        return (
            <TableRow 
                key={`row-${i}`} 
                rowData={rowData} 
                rowIndex={i} 
                rowOnClickCb={rowOnClickCb}
                rowOnDbClickCb={rowOnDbClickCb}
                selectable
                selected={rowIsSelected}
            >{cells}</TableRow>
        );
    });
    if (!data.length) {
        body = <EmptyTableRow colSpan={columnsDef.length} />;
    }

    return (
        <table className={classes.Table}>
            <thead><tr>{headers}</tr></thead>
            <tbody>{body}</tbody>
        </table>
    );
};

Table.propTypes = {
    columnsDef: PropTypes.arrayOf(types.columnDef).isRequired,
    data: PropTypes.arrayOf(types.dataRowDef),
    rowOnClickCb: PropTypes.func,
    rowOnDbClickCb: PropTypes.func,
    selectable: PropTypes.bool,
    selectedRowIndex: PropTypes.number,
};

Table.defaultProps = {
    data: [],
    selectedRowIndex: -1,
};

export default Table;
