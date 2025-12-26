import MainIcon from '../MainIcon';
import Tools from './Tools';

const Header = () => {
  return (
    <header
      className="
      page bg-brand-primary py-2
      text-text-inverse
      flex flex-row gap-8 items-center
      "
    >
      <MainIcon className="w-10 h-10" />
      <Tools />
    </header>
  );
};

export default Header;
