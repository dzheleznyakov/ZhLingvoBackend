import PropTypes from 'prop-types';

export const columnDef = PropTypes.shape({
    name: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    cellOnClickCb: PropTypes.func,
})

export const dataCellDef = PropTypes.shape({
    value: PropTypes.node.isRequired,
});

export const dataRowDef = PropTypes.objectOf(dataCellDef);
