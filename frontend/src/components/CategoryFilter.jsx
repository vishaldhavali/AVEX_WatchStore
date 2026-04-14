import './CategoryFilter.css';

const CATEGORIES = ['ALL', 'LUXURY', 'SPORT', 'CASUAL', 'SMART'];

const CategoryFilter = ({ selected, onChange }) => {
  return (
    <div className="category-filter">
      {CATEGORIES.map((cat) => (
        <button
          key={cat}
          className={`category-filter__btn${selected === cat ? ' active' : ''}`}
          onClick={() => onChange(cat)}
        >
          {cat}
        </button>
      ))}
    </div>
  );
};

export default CategoryFilter;
