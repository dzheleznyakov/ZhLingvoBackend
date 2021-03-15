import React from 'react';
import PropTypes from 'prop-types';

import classes from './TableRow.module.scss';

const EmptyTableRow = props => {
    const { colSpan } = props;
    return (
        <tr className={classes.Empty}>
            <td colSpan={colSpan}>No data, the table is empty</td>
        </tr>
    );
};

EmptyTableRow.propTypes = {
    colSpan: PropTypes.number,
};

export default EmptyTableRow;
