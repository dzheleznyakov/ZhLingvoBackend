import React from 'react';
import PropTypes from 'prop-types';

import classes from './ListView.module.scss';

import ListView from './ListView';

const ResusableListView = props => {
    const { items, onItemClick, width, height, selectedIndex } = props;
    
    const getItemClassName = index => (index === selectedIndex ? classes.SelectedWord : null);
    const style = { width, minHeight: height, height };

    const itemMapper = ({ key, node }, i) => (
        <li 
            key={key}
            className={getItemClassName(i)}
            onClick={onItemClick(i)}
        >
            {node}
        </li>
    );

    return (
        <ul className={classes.ListViewPort} style={style}>
            {items.map(itemMapper)}
        </ul>
    );
};

console.log(ListView.propTypes)

ResusableListView.propTypes = ListView.propTypes;

ResusableListView.defaultProps = ListView.defaultProps;

export default ResusableListView;
