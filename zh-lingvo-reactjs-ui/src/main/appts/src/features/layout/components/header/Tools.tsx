const Tools = () => {
  const style = 'px-1 leading-none font-medium hover:text-text-inverse/70';
  const active = 'pb-0.5 border-b-2 border-text-inverse/70 hover:border-text-inverse/50';
  const inactive = 'text-text-inverse/80';

  return (
    <nav className="flex flex-row gap-2">
      <li className={`${style} ${active}`}>
        <button>Dictionaries</button>
      </li>
      <li className={`${style} ${inactive}`}>
        <button>Tutor</button>
      </li>
    </nav>
  );
};

export default Tools;
