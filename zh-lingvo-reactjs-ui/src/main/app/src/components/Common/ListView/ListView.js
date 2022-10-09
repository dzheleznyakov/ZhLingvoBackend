import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import classes from './ListView.module.scss';

import * as selectors from '../../../store/selectors';

const ListView = props => {
    const { items, onItemClick } = props;
    const selectedWordIndex = useSelector(selectors.selectedWordIndexSelector);
    const getItemClassName = index => (index === selectedWordIndex ? classes.SelectedWord : null);
    return (
        <ul className={classes.ListViewPort}>
            {items.map(({ key, node }, i) => (
                <li 
                    key={key}
                    className={getItemClassName(i)}
                    onClick={onItemClick(i)}
                >
                    {node}
                </li>
            ))}
        </ul>
    );
};

ListView.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        key: PropTypes.string.isRequired,
        node: PropTypes.node.isRequired,
    })),
    onItemClick: PropTypes.func,
};

ListView.defaultProps = {
    items: [],
    onItemClick: () => {},
};

export default ListView;
